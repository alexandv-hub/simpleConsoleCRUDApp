package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Entity;
import com.consoleCRUDApp.repository.gson.GsonGenericRepositoryImpl;
import com.consoleCRUDApp.view.BaseEntityView;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public abstract class GenericEntityController<T extends Entity,
                                              R extends GsonGenericRepositoryImpl<T, Long>,
                                              V extends BaseEntityView>
                                    implements EntityController {

    protected final R repository;
    protected final V baseEntityView;


    public abstract void showEntitiesListFormatted(List<T> activeEntities);
    public abstract void saveNewEntity(T entity, String operationName);
    public abstract T prepareNewEntity();
    public abstract T requestEntityUpdatesFromUser(Long id);
    public abstract void cascadeUpdateEntity(T updatedEntity);


    @Override
    public void showMenu() {
        baseEntityView.startMenu();
    }

    @Override
    public void createAndSaveNewEntity() {
        String createOperationName = "create";
        baseEntityView.showInConsole("Starting create new entity...");

        T entity = prepareNewEntity();

        String entityClassSimpleName = entity.getClass().getSimpleName();

        showInfoMessageYouAreAboutTo(createOperationName, entityClassSimpleName, entity);
        if (userConfirmsOperation()) {
            saveNewEntity(entity, createOperationName);
        } else {
            showInfoMessageOperationCancelled(createOperationName, entityClassSimpleName);
        }
    }


    @Override
    public void findEntityById() {
        baseEntityView.showInConsole("Starting find entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        repository.findById(id).ifPresentOrElse(
                entity -> {
                    baseEntityView.printlnToConsole("\nFound " + getEntityClassName() + " entity:");
                    baseEntityView.printlnToConsole(entity.toString());
                },
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    @Override
    public void showAllActiveEntities() {
        baseEntityView.showInConsole("Starting show all entities...");
        List<T> activeEntities = repository.findAll();

        if (!activeEntities.isEmpty()) {
            baseEntityView.printlnToConsole("\nFound " + activeEntities.size() + " entities.");
            baseEntityView.printlnToConsole(
                    "All " + repository.getEntityClass().getSimpleName() + " entities: ");
            showEntitiesListFormatted(activeEntities);
        } else {
            baseEntityView.showInConsole("\nNo entities found!");
        }
    }

    @Override
    public void updateEntity() {
        baseEntityView.showInConsole("Starting update entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        repository.findById(id).ifPresentOrElse(
                entity -> processEntityUpdate(entity),
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    private void processEntityUpdate(T entity) {
        String updateOperationName = "update";
        String entityClassSimpleName = entity.getClass().getSimpleName();

        showInfoMessageYouAreAboutTo(updateOperationName, entityClassSimpleName, entity);
        if (userConfirmsOperation()) {
            T updatedEntity = requestEntityUpdatesFromUser(entity.getId());

            cascadeUpdateEntity(updatedEntity);

            showInfoMessageEntityOperationFinishedSuccessfully(updateOperationName, entity.getId());
        } else {
            showInfoMessageOperationCancelled(updateOperationName, entityClassSimpleName);
        }
    }

    boolean userConfirmsOperation() {
        String yesOrNoUserAnswer = baseEntityView.requestYesOrNoFromUser();
        return yesOrNoUserAnswer.equalsIgnoreCase(YES_USER_COMMAND);
    }

    @Override
    public void deleteEntityById() {
        baseEntityView.showInConsole("Starting delete entity by ID...");
        Long id = baseEntityView.promptEntityIdFromUser();
        repository.findById(id).ifPresentOrElse(
                entity -> processEntityDeletion(entity, id),
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    private void processEntityDeletion(T entity, Long id) {
        String operationName = "delete";
        String entityClassSimpleName = entity.getClass().getSimpleName();
        showInfoMessageYouAreAboutTo(operationName, entityClassSimpleName, entity);

        if (userConfirmsOperation()) {
            deleteEntity(operationName, id);
        } else {
            showInfoMessageOperationCancelled(operationName, entityClassSimpleName);
        }
    }

    private void deleteEntity(String operationName, Long id) {
        try {
            repository.deleteById(id);
            showInfoMessageEntityOperationFinishedSuccessfully(operationName, id);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void exit() {
        baseEntityView.outputExitedFromMainMenu(getEntityClassName());
    }


    private void showInfoMessageEntityWithIdNotFound(Long id) {
        baseEntityView.outputEntityWithIdNotFound(id);
    }

    void showInfoMessageOperationCancelled(String currOperationName, String entityClassSimpleName) {
        baseEntityView.outputEntityOperationCancelled(currOperationName, entityClassSimpleName);
    }

    private void showInfoMessageYouAreAboutTo(String operationName, String entityClassName, T entity) {
        baseEntityView.outputYouAreAboutTo(operationName, entityClassName, entity);
    }

    void showInfoMessageEntityOperationFinishedSuccessfully(String operationName, Long id) {
        String entityClassSimpleName = repository.getEntityClass().getSimpleName();
        baseEntityView.outputEntityOperationFinishedSuccessfully(operationName, entityClassSimpleName, id);
    }

    void showInfoMessageEntityAlreadyExists(String entityClassSimpleName,
                                            String entityFieldName,
                                            String entityMainFieldStringValue) {
        baseEntityView.outputMessageEntityAlreadyExists(entityClassSimpleName, entityFieldName, entityMainFieldStringValue);
    }

}

