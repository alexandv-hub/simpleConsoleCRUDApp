package com.consoleCRUDApp.controller;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.gson.GsonLabelRepositoryImpl;
import com.consoleCRUDApp.view.LabelView;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
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
    public Label prepareNewEntity(Long nextId, Status activeStatus) {
        String newLabelName = labelView.getLabelNameUserInput();
        return new Label(nextId, activeStatus, newLabelName);
    }

    @Override
    public Label requestEntityUpdatesFromUser(Long id) {
        String updatedLabelName = labelView.getUserInput(INPUT_THE_LABEL_NEW_NAME);
        return new Label(id, Status.ACTIVE, updatedLabelName);
    }

    @Override
    public void showEntitiesListFormatted(List<Label> activeEntities) {
        Character[] borderStyle = AsciiTable.FANCY_ASCII;
        List<ColumnData<Label>> columns = Label.getColumnData();
        String rend = AsciiTable.getTable(borderStyle, activeEntities, columns);
        labelView.showInConsole(rend);
    }

    @Override
    public void saveNewEntity(Label entity, String operationName) {
        repository.save(entity);
        repository.saveDataToRepositoryFile();
        showInfoMessageEntityOperationFinishedSuccessfully(operationName, entity.getId());
    }

    @Override
    public String getEntityClassName() {
        return ENTITY_CLASS_NAME;
    }

    public List<Label> promptLabelNamesListFromUser(Long startId) {
        List<Label> labelsListFromUserInput = new ArrayList<>();
        int counter = 1;
        long newLabelNextId = (startId != null) ? startId : generateNextId();

        String postLabelName = labelView.promptForLabelName(counter);
        while (!postLabelName.isBlank()) {
            Label newLabelEntity = new Label(newLabelNextId, Status.ACTIVE, postLabelName);
            labelsListFromUserInput.add(newLabelEntity);

            counter++;
            newLabelNextId++;
            postLabelName = labelView.promptForLabelName(counter);
        }
        return labelsListFromUserInput;
    }
}
