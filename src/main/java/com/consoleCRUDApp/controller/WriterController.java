package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonPostRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonWriterRepositoryImpl;
import com.consoleCRUDApp.view.WriterView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WriterController
        extends GenericEntityController<Writer, GsonWriterRepositoryImpl, WriterView> {

    private final String ENTITY_CLASS_NAME = "WRITER";

    private final GsonPostRepositoryImpl postRepository;
    private final GsonLabelRepositoryImpl labelRepository;

    private final WriterView writerView = baseEntityView;

    public static final String INPUT_THE_NEW_WRITER_FIRST_NAME = "\nPlease input the new Writer First Name: ";
    public static final String INPUT_THE_NEW_WRITER_LAST_NAME = "Please input the new Writer Last Name: ";

    public WriterController(GsonWriterRepositoryImpl writerRepository,
                            GsonPostRepositoryImpl postRepository,
                            GsonLabelRepositoryImpl labelRepository,
                            WriterView writerView) {
        super(writerRepository, writerView);
        this.postRepository = postRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public Writer prepareNewEntity() {
        String newWriterFirstName = writerView.getUserInput(INPUT_THE_NEW_WRITER_FIRST_NAME);
        String newWriterLastName = writerView.getUserInput(INPUT_THE_NEW_WRITER_LAST_NAME);

        List<Post> newWriterPosts = promptWriterPostsFromUser();

        return Writer.builder()
                .firstName(newWriterFirstName)
                .lastName(newWriterLastName)
                .posts(newWriterPosts)
                .build();
    }

    private List<Post> promptWriterPostsFromUser() {
        List<Post> postList = new ArrayList<>();

        writerView.showInConsole("\nWould you like to create the writer Posts?");
        if (userConfirmsOperation()) {
            int counter = 1;
            do {
                writerView.showInConsole("\nCreating Post " + counter + " for Writer entity...");
                Post newPost = Post.builder()
                            .title(writerView.getUserInput("\nEnter Post " + counter + " Title: "))
                            .content(writerView.getUserInput("Enter Post " + counter + " Content: "))
                            .labels(writerView.promptNewPostLabelsNamesFromUser())
                            .build();

                postList.add(newPost);

                counter++;
                writerView.showInConsole("\nWould you like to create the writer Post " + counter + "?");
            } while (userConfirmsOperation());
        }
        else {
            showInfoMessageOperationCancelled("create", getEntityClassName());
        }
        return postList;
    }


    @Override
    public Writer requestEntityUpdatesFromUser(Long id) {
        String newWriterFirstName = writerView.getUserInput(
                "Please input the Writer new First Name: ");
        String newWriterLastName = writerView.getUserInput(
                "Please input the Writer new Last Name: ");

        List<Post> writerPosts = new ArrayList<>();
            Optional<Writer> writerOptional = repository.findById(id);
            if (writerOptional.isPresent()) {
                Writer writer = writerOptional.get();
                writerPosts = writer.getPosts();
            }

        return new Writer(id,
                Status.ACTIVE,
                newWriterFirstName,
                newWriterLastName,
                writerPosts
        );
    }

    @Override
    public void cascadeUpdateEntity(Writer updatedWriter) {
        repository.update(updatedWriter);
        updatedWriter.getPosts().forEach(post -> {
            postRepository.save(post);
            post.getLabels().forEach(labelRepository::save);
        });
    }

    @Override
    public void showEntitiesListFormatted(List<Writer> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = Writer.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        writerView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Writer entity, String operationName) {
        entity.getPosts().forEach(
                post -> post.getLabels().forEach(labelRepository::save));

        entity.getPosts().forEach(postRepository::save);

        repository.save(entity);

        showInfoMessageEntityOperationFinishedSuccessfully(operationName, entity.getId());
    }

    @Override
    public String getEntityClassName() {
        return ENTITY_CLASS_NAME;
    }

}
