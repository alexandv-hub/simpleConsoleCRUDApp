package com.consoleCRUDApp;

import com.consoleCRUDApp.controller.LabelController;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.controller.WriterController;
import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonPostRepositoryImpl;
import com.consoleCRUDApp.repository.gson.GsonWriterRepositoryImpl;
import com.consoleCRUDApp.view.LabelView;
import com.consoleCRUDApp.view.PostView;
import com.consoleCRUDApp.view.WriterView;
import lombok.Getter;

@Getter
public class ApplicationContext {

    @Getter
    private static ApplicationContext instance = new ApplicationContext();

    private final String writersFilePath = "src/main/resources/data/writers.json";
    private final String postsFilePath = "src/main/resources/data/posts.json";
    private final String labelsFilePath = "src/main/resources/data/labels.json";

    private final WriterController writerController;
    private final PostController postController;
    private final LabelController labelController;

    private ApplicationContext() {
        GsonLabelRepositoryImpl labelRepository = new GsonLabelRepositoryImpl(Label.class, labelsFilePath);
        GsonPostRepositoryImpl postRepository = new GsonPostRepositoryImpl(Post.class, postsFilePath);
        GsonWriterRepositoryImpl writerRepository = new GsonWriterRepositoryImpl(Writer.class, writersFilePath);

        WriterView writerView = new WriterView();
        PostView postView = new PostView();
        LabelView labelView = new LabelView();

        labelController = new LabelController(labelRepository, labelView);
        postController = new PostController(postRepository, labelRepository, postView);
        writerController = new WriterController(writerRepository, postRepository, labelRepository, writerView);
    }

}
