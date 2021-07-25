package com.stilyanov.repository.controller;

import com.stilyanov.repository.config.JwtUtils;
import com.stilyanov.repository.exception.DuplicateEntityException;
import com.stilyanov.repository.exception.EntityNotFoundException;
import com.stilyanov.repository.exception.InvalidOperationException;
import com.stilyanov.repository.mapper.Mapper;
import com.stilyanov.repository.model.Repository;
import com.stilyanov.repository.model.User;
import com.stilyanov.repository.model.dto.RepositoryDto;
import com.stilyanov.repository.service.contract.RepositoryService;
import com.stilyanov.repository.service.contract.UserService;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/repositories")
public class RepositoryResource {

    private final RepositoryService repositoryService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public RepositoryResource(RepositoryService repositoryService, UserService userService, JwtUtils jwtUtils) {
        this.repositoryService = repositoryService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public List<Repository> getAll(){
        return repositoryService.getAllRepositories();
    }

    @GetMapping("/{Id}")
    public Repository getRepositoryById(@PathVariable long repositoryId){
        try{
            return repositoryService.getById(repositoryId);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Repository createRepo(@Valid @RequestBody RepositoryDto repositoryDto, Principal principal) throws IOException {
        try {
            GitHub github = GitHub.connectUsingOAuth(jwtUtils.jwt);

            GHRepository ghRepository = getRepoBuilder(repositoryDto, principal, github);

            Repository repository = new Repository();
            repository.setName(ghRepository.getName());
            repository.setDescription(ghRepository.getDescription());

            User user = new User();
            user.setAvatar(ghRepository.getOwner().getAvatarUrl());
            user.setUsername(ghRepository.getOwner().getName());
            repository.setOwner(user);

            repositoryService.create(repository);
            userService.create(user);
            return repository;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Repository update(@PathVariable long id, @Valid @RequestBody RepositoryDto repositoryDto, Principal principal) {
        try {
            User user = userService.getByName(principal.getName());
            Repository repositoryToUpdate = repositoryService.getById(id);
            Mapper.toRepository(repositoryDto, repositoryToUpdate);
            repositoryService.update(repositoryToUpdate, user);
            return repositoryToUpdate;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable long id, Principal principal) {
        try {
            Repository repository = repositoryService.getById(id);
            User user = userService.getByName(principal.getName());
            repositoryService.delete(repository, user);
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private GHRepository getRepoBuilder(RepositoryDto repositoryDto, Principal principal, GitHub github) throws IOException {
        GHCreateRepositoryBuilder repoBuilder = github.createRepository(repositoryDto.getName())
                .description(repositoryDto.getDescription()).owner(principal.getName());
        repoBuilder.private_(false);
        repoBuilder.create();
        return repoBuilder.create();
    }


}
