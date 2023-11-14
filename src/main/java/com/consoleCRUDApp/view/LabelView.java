package com.consoleCRUDApp.view;

import com.consoleCRUDApp.ApplicationContext;
import com.consoleCRUDApp.controller.LabelController;
import com.consoleCRUDApp.model.Label;

import java.util.List;

import static com.consoleCRUDApp.controller.EntityController.GO_BACK_TO_MAIN_MENU_COMMAND;

public class LabelView extends BaseEntityView {

    private LabelController labelController;
    private void ensureControllerIsInitialized() {
        if (labelController == null) {
            labelController = ApplicationContext.getInstance().getLabelController();
        }
    }

    private final String INPUT_NEW_LABEL_NAME = "\nPlease input new Label Name: ";

    @Override
    public void startMenu() {
        ensureControllerIsInitialized();
        showConsoleEntityMenu(labelController.getEntityClassName());

        String inputCommand = getUserInputCommand();
        while (!inputCommand.equals(GO_BACK_TO_MAIN_MENU_COMMAND)) {
            labelController.executeMenuUserCommand(inputCommand);
            inputCommand = getUserInputCommand();
        }
        labelController.exit();
        showConsoleMainMenu();
    }


    public String getLabelNameUserInput() {
        return getUserInput(INPUT_NEW_LABEL_NAME);
    }

    public List<Label> getLabelNamesListFromUser(Long startId) {
        ensureControllerIsInitialized();
        return labelController.promptLabelNamesListFromUser(startId);
    }

    public String promptForLabelName(int labelNumber) {
        return getUserInput(
                "Please input the Post Label " + labelNumber + " Name (or empty input to stop entering the new writer post Labels): ").trim();
    }

    public List<Label> getLabelNamesListFromUser() {
        return getLabelNamesListFromUser(null);
    }
}
