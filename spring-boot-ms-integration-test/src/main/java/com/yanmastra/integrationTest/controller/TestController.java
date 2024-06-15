package com.yanmastra.integrationTest.controller;

import com.yanmastra.integrationTest.entity.TestIntegrationEntity;
import com.yanmastra.integrationTest.repo.TIEntityRepository;
import com.yanmastra.msSecurityBase.crud.BaseCrudController;
import com.yanmastra.msSecurityBase.crud.PagingJpaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/integration-test")
@SecurityRequirements({
        @SecurityRequirement(name = "Keycloak"),
        @SecurityRequirement(name = "Keycloak2"),
})
public class TestController extends BaseCrudController<TestIntegrationEntity, TestIntegrationEntity> {

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
        entity.setCreatedBy(s.getCreatedBy());
        entity.setName(s.getName());
        entity.setPrice(s.getPrice());
        entity.setCategoryEntity(s.getCategoryEntity());
        return s;
    }

    @Override
    protected Class<TestIntegrationEntity> getEntityClass() {
        return TestIntegrationEntity.class;
    }

    @GetMapping("/profile")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @Override
    protected Set<String> getSearchAbleColumn() {
        return Set.of("name", "price");
    }
}
