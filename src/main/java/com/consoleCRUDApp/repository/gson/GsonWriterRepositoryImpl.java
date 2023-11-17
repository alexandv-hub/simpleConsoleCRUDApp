package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.WriterRepository;
import lombok.Getter;

@Getter
public class GsonWriterRepositoryImpl
                    extends GsonGenericRepositoryImpl<Writer, Long>
                    implements WriterRepository {

    private static final Class<Writer> entityClass = Writer.class;
    private static final String WRITERS_FILE_PATH = "src/main/resources/data/writers.json";

    public GsonWriterRepositoryImpl() {
        super(entityClass, WRITERS_FILE_PATH);
    }

}
