package com.stilyanov.repository.service.contract;

import com.stilyanov.repository.model.Repository;
import com.stilyanov.repository.model.User;

import java.util.List;

public interface RepositoryService {

    void create(Repository repository);

    void update(Repository repository, User user);

    void delete(Repository repository, User User);

    List<Repository> getAllRepositories();

    Repository getById(long id);
}
