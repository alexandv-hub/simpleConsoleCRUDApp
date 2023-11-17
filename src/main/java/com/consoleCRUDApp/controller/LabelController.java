package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.view.LabelView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.List;

public class LabelController
                        extends GenericEntityController<Label, GsonLabelRepositoryImpl, LabelView> {

    private final String ENTITY_CLASS_NAME = "LABEL";

    private final LabelView labelView = baseEntityView;

    public static final String INPUT_THE_LABEL_NEW_NAME = "Please input the Label new Name: ";

    public LabelController(GsonLabelRepositoryImpl repository, LabelView labelView) {
        super(repository, labelView);
    }

    @Override
    public Label prepareNewEntity() {
        String newLabelName = labelView.promptNewLabelNameFromUser();

        return Label.builder()
                .name(newLabelName)
                .build();
    }

    @Override
    public void saveNewEntity(Label newLabelToSave, String operationName) {
        if (!repository.isEntityExistInRepository(newLabelToSave)) {
            repository.save(newLabelToSave);
            showInfoMessageEntityOperationFinishedSuccessfully(operationName, newLabelToSave.getId());
        }
        else {
            String entityClassSimpleName = repository.getEntityClass().getSimpleName();
            showInfoMessageEntityAlreadyExists(entityClassSimpleName, "Name", newLabelToSave.getName());
        }
    }

    @Override
    public Label requestEntityUpdatesFromUser(Long id) {
        String updatedLabelName = labelView.getUserInput(INPUT_THE_LABEL_NEW_NAME);

        return Label.builder()
                .id(id)
                .name(updatedLabelName)
                .status(Status.ACTIVE)
                .build();
    }

    @Override
    public void showEntitiesListFormatted(List<Label> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = Label.getColumnDataWithIds();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        labelView.showInConsole(rend);
    }

    @Override
    public void cascadeUpdateEntity(Label updatedLabel) {
        repository.update(updatedLabel);
    }

    @Override
    public String getEntityClassName() {
        return ENTITY_CLASS_NAME;
    }
}
