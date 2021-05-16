package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.ResourceType;
import com.icloud.corespringsecurity.domain.entity.Resources;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.icloud.corespringsecurity.domain.entity.QResources.*;

@Repository
public class ResourcesQueryDslRepository {

    private JPAQueryFactory queryFactory;

    public ResourcesQueryDslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Resources> getAllResources() {
        return queryFactory
                .select(resources)
                .from(resources)
                .join(resources.roleList).fetchJoin()
                .where(resources.resourceType.eq(ResourceType.URL))
                .orderBy(resources.orderNum.desc())
                .fetch();
    }
}
