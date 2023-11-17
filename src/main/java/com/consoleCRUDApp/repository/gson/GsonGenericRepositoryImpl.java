package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Entity;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.GenericRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class GsonGenericRepositoryImpl<T extends Entity, ID>
                                            implements GenericRepository<T, ID> {
    private final Class<T> entityClass;
    private final String filePath;
    private final Gson gson;

    public GsonGenericRepositoryImpl(Class<T> entityClass, String filePath) {
        this.entityClass = entityClass;
        this.filePath = filePath;
        this.gson = new Gson();
    }


    @Override
    public T save(T entity) {
        List<T> entities = findAll();

        long nextId = generateEntityNextId();
        entity.setId(nextId);
        entity.setStatus(Status.ACTIVE);

        entities.add(entity);
        saveEntitiesToFile(entities);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return findAll().stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        List<T> entities = readDataFromFile(filePath);

        return Optional.ofNullable(entities).orElseGet(Collections::emptyList).stream()
                .filter(entity -> entity.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    private List<T> readDataFromFile(String filePath) {
        List<T> entities;
        Path path = Paths.get(filePath);
        if (Files.notExists(path) || !Files.isReadable(path)) {
            throw new IllegalStateException("Cannot load data, file does not exist or cannot be read: " + filePath);
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            Type typeToken = TypeToken.getParameterized(List.class, entityClass).getType();
            entities = gson.fromJson(bufferedReader, typeToken);
        } catch (IOException e) {
            throw new IllegalStateException("IO Exception loading data from file: " + filePath, e);
        }
        return entities;
    }

    @Override
    public Optional<T> update(T entity) {
        List<T> entities = findAll();

        List<T> updatedEntities = entities.stream()
                .map(existingEntity -> existingEntity.getId().equals(entity.getId()) ? entity : existingEntity)
                .collect(Collectors.toList());

        saveEntitiesToFile(updatedEntities);

        return updatedEntities.stream()
                .filter(updatedEntity -> updatedEntity.getId().equals(entity.getId()))
                .findFirst();
    }

    @Override
    public void deleteById(ID id) {
        List<T> entities = findAll().stream()
                .peek(entity -> {
                    if (entity.getId().equals(id)) {
                        entity.setStatus(Status.DELETED);
                    }
                })
                .collect(Collectors.toList());

        saveEntitiesToFile(entities);
    }


    private void saveEntitiesToFile(List<T> entities) {
        Path path = Paths.get(filePath);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            gson.toJson(entities, bufferedWriter);
        } catch (IOException e) {
            throw new IllegalStateException("IO Exception writing entities to file: " + filePath, e);
        }
    }

    private long generateEntityNextId() {
        List<T> baseEntityList = findAll();
        return baseEntityList.stream()
                .map(baseEntity -> baseEntity.getId())
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .map(id -> id + 1)
                .orElse(1L);
    }

}