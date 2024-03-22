package com.yanmastra.msSecurityBase.repo;


import com.yanmastra.msSecurityBase.crud.PagingJpaRepository;
import com.yanmastra.msSecurityBase.entity.TestIntegrationEntity;

public interface TIEntityRepository extends PagingJpaRepository<TestIntegrationEntity, String> {
}
