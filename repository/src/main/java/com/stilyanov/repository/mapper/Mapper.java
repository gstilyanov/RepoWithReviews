package com.stilyanov.repository.mapper;

import com.stilyanov.repository.model.Repository;
import com.stilyanov.repository.model.dto.RepositoryDto;

public class Mapper {

    public static void toRepository(RepositoryDto repositoryDto, Repository repository){
        repository.setDescription(repository.getDescription());
        repository.setName(repository.getName());
    }
}
