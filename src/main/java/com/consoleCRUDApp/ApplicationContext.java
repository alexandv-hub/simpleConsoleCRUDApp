package com.consoleCRUDApp;

import com.consoleCRUDApp.controller.LabelController;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.controller.WriterController;
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

    private final LabelController labelController;
    private final PostController postController;
    private final WriterController writerController;

    private ApplicationContext() {
        GsonLabelRepositoryImpl labelRepository = new GsonLabelRepositoryImpl();
        GsonPostRepositoryImpl postRepository = new GsonPostRepositoryImpl();
        GsonWriterRepositoryImpl writerRepository = new GsonWriterRepositoryImpl();

        WriterView writerView = new WriterView();
        PostView postView = new PostView();
        LabelView labelView = new LabelView();

        labelController = new LabelController(labelRepository, labelView);
        postController = new PostController(postRepository, labelRepository, postView);
        writerController = new WriterController(writerRepository, postRepository, labelRepository, writerView);
    }

}
