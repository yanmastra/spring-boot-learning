package com.yanmastra.msSecurityBase.controller;

import com.yanmastra.msSecurityBase.crud.CrudEndpoint;
import com.yanmastra.msSecurityBase.crud.PagingJpaRepository;
import com.yanmastra.msSecurityBase.entity.TestIntegrationEntity;
import com.yanmastra.msSecurityBase.repo.TIEntityRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/integration-test")
@SecurityRequirement(name = "Keycloak")
public class TestController extends CrudEndpoint<TestIntegrationEntity, TestIntegrationEntity> {

    @Autowired
    TIEntityRepository tiEntityRepository;

    @Override
    protected PagingJpaRepository<TestIntegrationEntity, String> getRepository() {
        return tiEntityRepository;
    }

    @Override
    protected TestIntegrationEntity fromEntity(TestIntegrationEntity entity) {
        return entity;
    }

    @Override
    protected TestIntegrationEntity toEntity(TestIntegrationEntity s) {
        return s;
    }

    @Override
    protected TestIntegrationEntity update(TestIntegrationEntity entity, TestIntegrationEntity s) {
        return s;
    }

    @GetMapping("/profile")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
