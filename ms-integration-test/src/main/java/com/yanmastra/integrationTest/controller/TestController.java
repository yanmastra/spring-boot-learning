package com.yanmastra.integrationTest.controller;

import com.yanmastra.integrationTest.entity.TestIntegrationEntity;
import com.yanmastra.integrationTest.repo.TIEntityRepository;
import com.yanmastra.microservices.crud.CrudEndpoint;
import com.yanmastra.microservices.crud.PagingJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("integration-test")
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
}
