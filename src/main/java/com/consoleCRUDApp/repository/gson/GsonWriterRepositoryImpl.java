package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.WriterRepository;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;

@Getter
public class GsonWriterRepositoryImpl
                    extends GsonGenericRepositoryImpl<Writer, Long>
                    implements WriterRepository {

    private final Class<Writer> entityClass = Writer.class;

    public GsonWriterRepositoryImpl(Class<Writer> entityClass, String filePath) {
        super(entityClass, filePath);
    }

    @Override
    public Type getEntityTypeToken() {
        return new TypeToken<List<Writer>>() {}.getType();
    }

}
