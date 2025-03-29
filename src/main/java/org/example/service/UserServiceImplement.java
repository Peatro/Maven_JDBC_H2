package org.example.service;

import org.example.model.Model;
import org.example.repository.ModelRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImplement implements UserService {
    private final ModelRepository modelRepository;

    public UserServiceImplement(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    public Model save(Model user) {
        return modelRepository.save(user);
    }

    @Override
    public Optional<Model> findById(int id) {
        return modelRepository.findById(id);
    }

    @Override
    public List<Model> findAll() {
        return modelRepository.findAll();
    }

    @Override
    public Model update(Model user) {
        return modelRepository.update(user);
    }

    @Override
    public boolean delete(int id) {
        return modelRepository.delete(id);
    }
}
