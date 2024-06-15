package com.yanmastra.integrationTest.repo;

import com.yanmastra.integrationTest.entity.TestIntegrationEntity;
import com.yanmastra.msSecurityBase.crud.PagingJpaRepository;

public interface TIEntityRepository extends PagingJpaRepository<TestIntegrationEntity, String> {
}
