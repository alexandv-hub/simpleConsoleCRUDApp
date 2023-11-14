package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonPostRepositoryImpl;
import com.consoleCRUDApp.view.LabelView;
import com.consoleCRUDApp.view.PostView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostController
        extends GenericEntityController<Post, GsonPostRepositoryImpl, PostView> {

    private final String ENTITY_CLASS_NAME = "POST";

    private final GsonLabelRepositoryImpl labelRepository;
    private final PostView postView = baseEntityView;
    private LabelView labelView;

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
    public Post prepareNewEntity(Long nextId, Status activeStatus) {
        String newPostTitle = postView.getUserInput(INPUT_THE_NEW_POST_TITLE);
        String newPostContent = postView.getUserInput(INPUT_THE_NEW_POST_CONTENT);
        List<Label> newPostLabelList = getPostLabelListFromUserInputs();

        return new Post(nextId,
                        activeStatus,
                        newPostTitle,
                        newPostContent,
                        newPostLabelList
        );
    }

    public Post prepareNewEntity(Long nextPostId, Long nextLabelId) {
        String newPostTitle = postView.getUserInput(INPUT_THE_NEW_POST_TITLE);
        String newPostContent = postView.getUserInput(INPUT_THE_NEW_POST_CONTENT);
        List<Label> newPostLabelList = getPostLabelListFromUserInputs(nextLabelId);

        return new Post(nextPostId,
                        Status.ACTIVE,
                        newPostTitle,
                        newPostContent,
                        newPostLabelList
        );
    }

    private List<Label> getPostLabelListFromUserInputs() {
        labelView = new LabelView();
        return labelView.getLabelNamesListFromUser();
    }

    private List<Label> getPostLabelListFromUserInputs(Long nextLabelId) {
        return labelView.getLabelNamesListFromUser(nextLabelId);
    }

    @Override
    public Post requestEntityUpdatesFromUser(Long id) {
        String updatedPostTitle = postView.getUserInput(INPUT_THE_POST_NEW_TITLE);
        String updatedPostContent = postView.getUserInput(INPUT_THE_POST_NEW_CONTENT);

        List<Label> labelList = new ArrayList<>();
        postView.showInConsole(
                "Would you like to update the Post labels?");
        if (userConfirmsOperation()) {
            labelList = getPostLabelListFromUserInputs();
        }
        else {
            Optional<Post> postOptional = repository.findById(id);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                labelList = post.getLabels();
            }
        }

        return new Post(id,
                Status.ACTIVE,
                updatedPostTitle,
                updatedPostContent,
                labelList
        );
    }

    @Override
    public void showEntitiesListFormatted(List<Post> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Post>> columns = Post.getColumnData();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        postView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Post entity, String operationName) {

        entity.getLabels().forEach(labelRepository::save);
        labelRepository.saveDataToRepositoryFile();

        repository.save(entity);
        repository.saveDataToRepositoryFile();

        showInfoMessageEntityOperationFinishedSuccessfully(operationName, entity.getId());
    }

    @Override
    public String getEntityClassName() {
        return ENTITY_CLASS_NAME;
    }

}
