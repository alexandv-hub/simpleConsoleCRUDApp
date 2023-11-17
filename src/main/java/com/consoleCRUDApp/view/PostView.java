package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.PostController;
import com.consoleCRUDApp.model.Label;

import java.util.ArrayList;
import java.util.List;

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

    public List<Label> promptPostLabelsNamesFromUser() {
        List<Label> labelsListFromUserInput = new ArrayList<>();

        int counter = 1;
        String postLabelName = promptLabelNameFromUser(counter);

        while (!postLabelName.isBlank()) {
            Label newLabelEntity = Label.builder()
                    .name(postLabelName)
                    .build();

            labelsListFromUserInput.add(newLabelEntity);

            counter++;
            postLabelName = promptLabelNameFromUser(counter);
        }
        return labelsListFromUserInput;
    }

    public String promptLabelNameFromUser(int labelCounter) {
        return getUserInput(
                "Please input the Post Label " + labelCounter + " Name (or empty input to stop entering the new writer post Labels): ").trim();
    }

}
