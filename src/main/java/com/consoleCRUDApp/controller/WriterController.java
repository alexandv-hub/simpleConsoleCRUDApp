package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonPostRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonWriterRepositoryImpl;
import com.consoleCRUDApp.view.PostView;
import com.consoleCRUDApp.view.WriterView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class WriterController
        extends GenericEntityController<Writer, GsonWriterRepositoryImpl, WriterView> {

    private final String ENTITY_CLASS_NAME = "WRITER";

    private final GsonPostRepositoryImpl postRepository;
    private final GsonLabelRepositoryImpl labelRepository;

    private final WriterView writerView = baseEntityView;
    private final PostView postView = new PostView();

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
    public Writer prepareNewEntity(Long nextId, Status activeStatus) {
        String newWriterFirstName = writerView.getUserInput(INPUT_THE_NEW_WRITER_FIRST_NAME);
        String newWriterLastName = writerView.getUserInput(INPUT_THE_NEW_WRITER_LAST_NAME);
        long generatedNextId = postRepository.generateEntityNextId();
        List<Post> newWriterPosts = getNewWriterPostsList(generatedNextId);

        return new Writer(nextId,
                          activeStatus,
                          newWriterFirstName,
                          newWriterLastName,
                          newWriterPosts
        );
    }

    private List<Post> getNewWriterPostsList(Long startId) {
        List<Post> postList = new ArrayList<>();

        writerView.showInConsole(
                "\nWould you like to create the writer Posts list?");
        if (userConfirmsOperation()) {
            int counter = 1;
            long nextId = (startId != null) ? startId : postRepository.generateEntityNextId();
//            long nextId = repository.generateEntityNextId();
            do {
                writerView.showInConsole(
                        "\nCreating Post " + counter + " for Writer entity...");
                Post newPost;
                if (counter > 1) {
                    Long nextLabelId = postList.stream()
                            .flatMap(post -> post.getLabels().stream()) // Преобразование List<Post> в Stream<Label>
                            .map(Label::getId)
                            .max(Comparator.naturalOrder())
                            .map(id -> id + 1)
                            .orElse(1L);
                    newPost = postView.getNewPostPrepared(nextId, nextLabelId);
                }
                else {
                    newPost = postView.getNewPostPrepared(nextId);
                }

                postList.add(newPost);
                counter++;
                nextId++;
                writerView.showInConsole(
                        "\nWould you like to create the writer Post " + counter + "?");
            } while (userConfirmsOperation());
        }
        else {
            showInfoMessageOperationCancelled("create", getEntityClassName());
        }

        return postList;
    }

    public List<Post> getWriterPostListUserInput() {
        return getNewWriterPostsList(null);
    }

    @Override
    public Writer requestEntityUpdatesFromUser(Long id) {
        String newWriterFirstName = writerView.getUserInput(
                "Please input the Writer new First Name: ");
        String newWriterLastName = writerView.getUserInput(
                "Please input the Writer new Last Name: ");

        List<Post> postList = new ArrayList<>();
        writerView.showInConsole("\nWould you like to update the Writer posts?");
        if (userConfirmsOperation()) {
            long generatedNextId = postRepository.generateEntityNextId();
            postList = getNewWriterPostsList(generatedNextId);
        }
        else {
            Optional<Writer> writerOptional = repository.findById(id);
            if (writerOptional.isPresent()) {
                Writer writer = writerOptional.get();
                postList = writer.getPosts();
            }
        }

        return new Writer(id,
                Status.ACTIVE,
                newWriterFirstName,
                newWriterLastName,
                postList
        );
    }

    @Override
    public void cascadeUpdateEntity(Writer updatedWriter) {
        repository.update(updatedWriter);
        updatedWriter.getPosts().forEach(post -> {
            postRepository.save(post);
            post.getLabels().forEach(labelRepository::save);
        });
        labelRepository.saveDataToRepositoryFile();
    }

    @Override
    public void showEntitiesListFormatted(List<Writer> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Writer>> columns = Writer.getColumnData();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        writerView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Writer entity, String operationName) {
        entity.getPosts().forEach(
                post -> post.getLabels().forEach(labelRepository::save));
        labelRepository.saveDataToRepositoryFile();

        entity.getPosts().forEach(postRepository::save);
        postRepository.saveDataToRepositoryFile();

        repository.save(entity);
        repository.saveDataToRepositoryFile();

        showInfoMessageEntityOperationFinishedSuccessfully(operationName, entity.getId());
    }

    @Override
    public String getEntityClassName() {
        return ENTITY_CLASS_NAME;
    }

}
