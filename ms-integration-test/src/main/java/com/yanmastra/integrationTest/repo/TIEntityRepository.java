package com.yanmastra.integrationTest.repo;

import com.yanmastra.integrationTest.entity.TestIntegrationEntity;
import com.yanmastra.microservices.crud.PagingJpaRepository;

public interface TIEntityRepository extends PagingJpaRepository<TestIntegrationEntity, String> {
}
