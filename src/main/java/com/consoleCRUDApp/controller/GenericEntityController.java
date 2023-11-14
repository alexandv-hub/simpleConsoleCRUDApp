package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.BaseEntity;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.GenericRepository;
import com.consoleCRUDApp.view.BaseEntityView;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class GenericEntityController<T extends BaseEntity<Long>,
                                              R extends GenericRepository<T, Long>,
                                              V extends BaseEntityView>
                                    implements EntityController {

    protected final R repository;
    protected final V baseEntityView;

    public GenericEntityController(R repository, V baseEntityView) {
        this.repository = repository;
        this.baseEntityView = baseEntityView;
    }

    public abstract void showEntitiesListFormatted(List<T> entities);
    public abstract void saveNewEntity(T entity, String operationName);
    public abstract T prepareNewEntity(Long generatedId, Status activeStatus);
    public abstract T requestEntityUpdatesFromUser(Long id);
    public abstract void cascadeUpdateEntity(T updatedEntity);

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

    @Override
    public void showMenu() {
        baseEntityView.startMenu();
    }

    // CREATE
    @Override
    public void createAndSaveNewEntity() {
        baseEntityView.showInConsole(
                "Starting create new entity...");
        String createOperationName = "create";

        T entity = prepareNewEntity();
        String entityClassSimpleName = entity.getClass().getSimpleName();

        showInfoMessageYouAreAboutTo(createOperationName, entityClassSimpleName, entity);
        if (userConfirmsOperation()) {
            saveNewEntity(entity, createOperationName);
        } else {
            showInfoMessageOperationCancelled(createOperationName, entityClassSimpleName);
        }
    }

    public T prepareNewEntity() {
        long generatedNextId = repository.generateEntityNextId();
        return prepareNewEntity(generatedNextId, Status.ACTIVE);
    }

    public T prepareNewEntity(Long id) {
        return prepareNewEntity(id, Status.ACTIVE);
    }


    // READ
    @Override
    public void findEntityById() {
        baseEntityView.showInConsole("Starting find entity by ID...");
        Long id = baseEntityView.requestEntityIdFromUser();
        repository.findById(id).ifPresentOrElse(
                entity -> {
                    baseEntityView.printlnToConsole(
                            "\nFound " + getEntityClassName() + " entity:");
                    baseEntityView.printlnToConsole(entity.toStringEntityTableView());
                },
                () -> showInfoMessageEntityWithIdNotFound(id)
        );
    }

    @Override
    public void showAllActiveEntities() {
        baseEntityView.showInConsole("Starting show all entities...");
        List<T> activeEntities = repository.findAll().stream()
                .filter(entity -> entity.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());

        if (!activeEntities.isEmpty()) {
            baseEntityView.printlnToConsole(
                    "\nFound " + activeEntities.size() + " entities.");
            baseEntityView.printlnToConsole(
                    "All " + repository.getEntityClass().getSimpleName() + " entities: ");
            showEntitiesListFormatted(activeEntities);
        } else {
            baseEntityView.showInConsole("\nNo entities found!");
        }
    }


    // UPDATE
    @Override
    public void updateEntity() {
        baseEntityView.showInConsole("Starting update entity by ID...");
        Long id = baseEntityView.requestEntityIdFromUser();
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
            repository.saveDataToRepositoryFile();
            showInfoMessageEntityOperationFinishedSuccessfully(updateOperationName, entity.getId());
        } else {
            showInfoMessageOperationCancelled(updateOperationName, entityClassSimpleName);
        }
    }

    boolean userConfirmsOperation() {
        String yesOrNoUserAnswer = baseEntityView.requestYesOrNoFromUser();
        return yesOrNoUserAnswer.equalsIgnoreCase(YES_USER_COMMAND);
    }


    // DELETE
    @Override
    public void deleteEntityById() {
        baseEntityView.showInConsole("Starting delete entity by ID...");
        Long id = baseEntityView.requestEntityIdFromUser();
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
            repository.saveDataToRepositoryFile();
            showInfoMessageEntityOperationFinishedSuccessfully(operationName, id);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void exit() {
        repository.saveDataToRepositoryFile();
        baseEntityView.outputExitedFromMainMenu(getEntityClassName());
    }

    @Override
    public long generateNextId() {
        return repository.generateEntityNextId();
    }
}

