package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.repository.LabelRepository;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Getter
public class GsonLabelRepositoryImpl
                    extends GsonGenericRepositoryImpl<Label, Long>
                    implements LabelRepository {

    private final Class<Label> entityClass = Label.class;

    public GsonLabelRepositoryImpl(Class<Label> entityClass, String filePath) {
        super(entityClass, filePath);
    }

    @Override
    public Type getEntityTypeToken() {
        return new TypeToken<List<Label>>() {}.getType();
    }

    public Optional<Label> findLabelByName(String name) {
        return idToEntityMap.values().stream()
                .filter(label -> label.getName().equals(name))
                .findAny();
    }
}
