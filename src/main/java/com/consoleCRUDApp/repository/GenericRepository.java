package com.consoleCRUDApp.repository;

import com.consoleCRUDApp.model.BaseEntity;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends BaseEntity<ID>, ID> {

    // CRUD methods
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    Optional<T> update(T entity);
    void deleteById(ID id);

    // Other methods
    Class<T> getEntityClass();
    Type getEntityTypeToken();
    long generateEntityNextId();
    void createIdToEntityMap(Type typeToken, Reader reader);
    void loadDataFromFile(String filePath);
    void saveDataToFile();
    void saveDataToRepositoryFile();
}
