package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.BaseEntity;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.GenericRepository;
import com.google.gson.Gson;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class GsonGenericRepositoryImpl<T extends BaseEntity<ID>, ID>
                                            implements GenericRepository<T, ID> {
    private final Class<T> entityClass;
    private final String filePath;
    private final Gson gson;
    Map<ID, T> idToEntityMap;

    public GsonGenericRepositoryImpl(Class<T> entityClass, String filePath) {
        this.entityClass = entityClass;
        this.filePath = filePath;
        this.gson = new Gson();
        this.idToEntityMap = new HashMap<>();
        loadDataFromFile(filePath);
    }


    // CRUD methods
    @Override
    public T save(T entity) {
        return idToEntityMap.put(entity.getId(), entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(idToEntityMap.get(id))
                .filter(entity -> entity.getStatus().equals(Status.ACTIVE));
    }

    @Override
    public List<T> findAll() {
        Collection<T> allEntities = idToEntityMap.values();
        return allEntities.stream()
                .filter(entity -> entity.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<T> update(T entity) {
        return Optional.ofNullable(idToEntityMap.replace(entity.getId(), entity));
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> entityToDelete = findById(id);
        entityToDelete.ifPresentOrElse(
                entity -> entity.setStatus(Status.DELETED),
                () -> { throw new NoSuchElementException(
                        "Entity " + getEntityClass().getSimpleName() + " with ID '" + id
                                + "' not found or is not active"); }
        );
    }


    // Other methods
    @Override
    public void createIdToEntityMap(Type typeToken, Reader reader) {
        List<T> entityList = gson.fromJson(reader, typeToken);
        idToEntityMap = Optional.ofNullable(entityList)
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.toMap(T::getId, entity -> entity));

    }

    @Override
    public void loadDataFromFile(String filePath) {
        Path path = Paths.get(filePath);

        if (Files.notExists(path) || !Files.isReadable(path)) {
            throw new IllegalStateException("Cannot load data, file does not exist or cannot be read: " + filePath);
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            Type typeToken = getEntityTypeToken();
            createIdToEntityMap(typeToken, bufferedReader);
        } catch (IOException e) {
            throw new IllegalStateException("IO Exception loading data from file: " + filePath, e);
        }
    }

    @Override
    public void saveDataToFile() {
        Path path = Paths.get(filePath);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            List<T> entityList = new ArrayList<>(idToEntityMap.values());
            gson.toJson(entityList, bufferedWriter);
        } catch (IOException e) {
            throw new IllegalStateException("IO Exception writing entities to file: " + filePath, e);
        }
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public long generateEntityNextId() {
        List<T> baseEntityList = findAll();
        return baseEntityList.stream()
                .map(baseEntity -> (Long)baseEntity.getId())
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .map(id -> id + 1)
                .orElse(1L);
    }

    @Override
    public void saveDataToRepositoryFile() {
        saveDataToFile();
    }

}