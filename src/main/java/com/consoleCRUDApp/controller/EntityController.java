package com.consoleCRUDApp.controller;

public interface EntityController extends BaseController {

    String CREATE_NEW_ENTITY_COMMAND = "1";
    String FIND_ENTITY_BY_ID_COMMAND = "2";
    String SHOW_ALL_ENTITIES_COMMAND = "3";
    String UPDATE_EXISTING_ENTITY_COMMAND = "4";
    String DELETE_EXISTING_ENTITY_COMMAND = "5";
    String GO_BACK_TO_MAIN_MENU_COMMAND = "6";
    String DEFAULT_RETURN_YOUR_INPUT_IS_NOT_A_COMMAND = "default return: your input is not a command";

    String YES_USER_COMMAND = "y";
    String NO_USER_COMMAND = "n";

    String getEntityClassName();

    void showMenu();
    void createAndSaveNewEntity();
    void findEntityById();
    void showAllActiveEntities();
    void updateEntity();
    void deleteEntityById();

    @Override
    default void executeMenuUserCommand(String inputCommand) {
        switch (inputCommand) {
            case CREATE_NEW_ENTITY_COMMAND:
                createAndSaveNewEntity();
                break;
            case FIND_ENTITY_BY_ID_COMMAND:
                findEntityById();
                break;
            case SHOW_ALL_ENTITIES_COMMAND:
                showAllActiveEntities();
                break;
            case UPDATE_EXISTING_ENTITY_COMMAND:
                updateEntity();
                break;
            case DELETE_EXISTING_ENTITY_COMMAND:
                deleteEntityById();
                break;
            case GO_BACK_TO_MAIN_MENU_COMMAND:
                exit();
                return;
            default:
                MAIN_VIEW.showInConsole(DEFAULT_RETURN_YOUR_INPUT_IS_NOT_A_COMMAND);
        }
    }

}
