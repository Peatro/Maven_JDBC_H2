package org.example.repository;

import org.example.model.Model;

import java.util.List;
import java.util.Optional;

public interface ModelRepository {
    Model save(Model user);

    Optional<Model> findById(int id);

    List<Model> findAll();

    Model update(Model user);

    boolean delete(int id);
}
