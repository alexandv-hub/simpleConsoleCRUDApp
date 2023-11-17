package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonPostRepositoryImpl;
import com.consoleCRUDApp.view.PostView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostController
        extends GenericEntityController<Post, GsonPostRepositoryImpl, PostView> {

    private final String ENTITY_CLASS_NAME = "POST";

    private final GsonLabelRepositoryImpl labelRepository;
    
    private final PostView postView = baseEntityView;

    public PostController(GsonPostRepositoryImpl postRepository,
                          GsonLabelRepositoryImpl labelRepository,
                          PostView postView) {
        super(postRepository, postView);
        this.labelRepository = labelRepository;
    }
    
    private final String INPUT_THE_NEW_POST_TITLE = "\nPlease input the new Post Title: ";
    private final String INPUT_THE_NEW_POST_CONTENT = "Please input the new Post Content: ";
    private final String INPUT_THE_POST_NEW_TITLE = "Please input the Post new Title: ";
    private final String INPUT_THE_POST_NEW_CONTENT = "Please input the Post new Content: ";


    @Override
    public Post prepareNewEntity() {
        String newPostTitle = postView.getUserInput(INPUT_THE_NEW_POST_TITLE);
        String newPostContent = postView.getUserInput(INPUT_THE_NEW_POST_CONTENT);

        List<Label> newPostLabels = postView.promptPostLabelsNamesFromUser();

        return Post.builder()
                .title(newPostTitle)
                .content(newPostContent)
                .labels(newPostLabels)
                .build();
    }

    @Override
    public void saveNewEntity(Post newPostToSave, String operationName) {
        List<Label> postLabels = newPostToSave.getLabels();
        postLabels = processPostLabelsIds(postLabels);
        newPostToSave.setLabels(postLabels);

        postLabels.forEach(label -> {
            if (!labelRepository.isEntityExistInRepository(label)) {
                labelRepository.save(label);
            }
        });
        repository.save(newPostToSave);
        showInfoMessageEntityOperationFinishedSuccessfully(operationName, newPostToSave.getId());
    }

    private List<Label> processPostLabelsIds(List<Label> postLabels) {
        return postLabels.stream()
                .map(label -> {
                    if (!labelRepository.isEntityExistInRepository(label)) {
                        return label;
                    } else {
                        return labelRepository.findLabelByName(label.getName())
                                .orElseThrow(() -> new RuntimeException("Label not found"));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Post requestEntityUpdatesFromUser(Long id) {
        String updatedPostTitle = postView.getUserInput(INPUT_THE_POST_NEW_TITLE);
        String updatedPostContent = postView.getUserInput(INPUT_THE_POST_NEW_CONTENT);

        List<Label> updatedLabelList = new ArrayList<>();
        postView.showInConsole("Would you like to update the Post labels?");
        if (userConfirmsOperation()) {
            updatedLabelList = postView.promptPostLabelsNamesFromUser();
        }
        else {
            Optional<Post> postOptional = repository.findById(id);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                updatedLabelList = post.getLabels();
            }
        }
        updatedLabelList = processPostLabelsIds(updatedLabelList);

        return new Post(id,
                Status.ACTIVE,
                updatedPostTitle,
                updatedPostContent,
                updatedLabelList
        );
    }

    @Override
    public void cascadeUpdateEntity(Post updatedEntity) {
        List<Label> updatedLabelList =  updatedEntity.getLabels();
        updatedLabelList.forEach(label -> {
            if (!labelRepository.isEntityExistInRepository(label)) {
                labelRepository.save(label);
            }
        });

        repository.update(updatedEntity);
    }

    @Override
    public void showEntitiesListFormatted(List<Post> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = Post.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        postView.showInConsole(rend);
    }

    @Override
    public String getEntityClassName() {
        return ENTITY_CLASS_NAME;
    }

}
