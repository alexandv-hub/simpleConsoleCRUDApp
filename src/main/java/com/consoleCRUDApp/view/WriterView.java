package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.WriterController;
import com.consoleCRUDApp.model.Label;

import java.util.ArrayList;
import java.util.List;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class WriterView extends BaseEntityView {

    private WriterController writerController;
    private void ensureControllerIsInitialized() {
        if (writerController == null) {
            writerController = ApplicationContext.getInstance().getWriterController();
        }
    }

    @Override
    public void startMenu() {
        ensureControllerIsInitialized();

        showConsoleEntityMenu(writerController.getEntityClassName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            writerController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        writerController.exit();
        showConsoleMainMenu();
    }

    public List<Label> promptNewPostLabelsNamesFromUser() {
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
