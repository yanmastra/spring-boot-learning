package com.yanmastra.integrationTest.controller;

import com.yanmastra.integrationTest.entity.TestIntegrationEntity;
import com.yanmastra.integrationTest.repo.TIEntityRepository;
import com.yanmastra.msSecurityBase.crud.CrudEndpoint;
import com.yanmastra.msSecurityBase.crud.Paginate;
import com.yanmastra.msSecurityBase.crud.PagingJpaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/integration-test")
@SecurityRequirements({
        @SecurityRequirement(name = "Keycloak"),
        @SecurityRequirement(name = "Keycloak2"),
})
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

    @Override
    public Paginate<TestIntegrationEntity> getList(Integer page, Integer size, Map<String, String> allParams, Principal principal) {
        return super.getList(page, size, allParams, principal);
    }
}
