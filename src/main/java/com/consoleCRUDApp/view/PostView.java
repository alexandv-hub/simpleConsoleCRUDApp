package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.model.Post;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class PostView extends BaseEntityView {

    private PostController postController;
    private void ensureControllerIsInitialized() {
        if (postController == null) {
            postController = ApplicationContext.getInstance().getPostController();
        }
    }

    @Override
    public void startMenu() {
        ensureControllerIsInitialized();
        showConsoleEntityMenu(postController.getEntityClassName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            postController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        postController.exit();
        showConsoleMainMenu();
    }

    public Post getNewPostPrepared(Long nextId) {
        ensureControllerIsInitialized();
        return postController.prepareNewEntity(nextId);
    }

    public Post getNewPostPrepared(Long nextPostId, Long nextLabelId) {
        ensureControllerIsInitialized();
        return postController.prepareNewEntity(nextPostId, nextLabelId);
    }

}
