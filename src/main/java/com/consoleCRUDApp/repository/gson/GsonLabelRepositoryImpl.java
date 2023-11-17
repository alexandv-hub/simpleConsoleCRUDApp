package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.repository.LabelRepository;
import lombok.Getter;

import java.util.Optional;

@Getter
public class GsonLabelRepositoryImpl
                    extends GsonGenericRepositoryImpl<Label, Long>
                    implements LabelRepository {

    private static final Class<Label> entityClass = Label.class;
    private static final String LABELS_FILE_PATH = "src/main/resources/data/labels.json";

    public GsonLabelRepositoryImpl() {
        super(entityClass, LABELS_FILE_PATH);
    }

    public boolean isEntityExistInRepository(Label label) {
        return findAll().stream()
                .anyMatch(existingLabel -> existingLabel.getName().equals(label.getName()));
    }

    public Optional<Label> findLabelByName(String name) {
        return findAll().stream()
                .filter(label -> label.getName().equals(name))
                .findAny();
    }
}
