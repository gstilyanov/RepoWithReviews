package com.stilyanov.repository.service;

import com.stilyanov.repository.exception.DuplicateEntityException;
import com.stilyanov.repository.exception.EntityNotFoundException;
import com.stilyanov.repository.exception.InvalidOperationException;
import com.stilyanov.repository.model.Repository;
import com.stilyanov.repository.model.User;
import com.stilyanov.repository.repository.RepositoryRepository;
import com.stilyanov.repository.service.contract.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    private final RepositoryRepository repositoryRepository;

    @Autowired
    public RepositoryServiceImpl(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    @Override
    public void create(Repository repository) {
        checkRepositoryNameUnique(repository);
        repositoryRepository.save(repository);
    }

    @Override
    public void update(Repository repository, User user) {
        checkUserCanEditRepository(repository, user);
        repositoryRepository.save(repository);
    }

    @Override
    public void delete(Repository repository, User user) {
        checkUserCanEditRepository(repository, user);
        repositoryRepository.delete(repository);
    }

    @Override
    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    @Override
    public Repository getById(long id) {
        if (repositoryRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("Repository with id %d not found!", id));
        }
        return repositoryRepository.findById(id).get();
    }

    private void checkRepositoryNameUnique(Repository repository) {
        if (repositoryRepository.findByName(repository.getName()).isPresent()) {
            throw new DuplicateEntityException(
                    String.format("Repository with name %s already exists!",
                            repository.getName())
            );
        }
    }

    private void checkUserCanEditRepository(Repository repository, User user) {
        if (repository.getOwner().getId() != user.getId()) {
            throw new InvalidOperationException(
                    String.format(
                            "User %s can't edit repository %s",
                            user.getUsername(),
                            repository.getName()));
        }
    }

}
