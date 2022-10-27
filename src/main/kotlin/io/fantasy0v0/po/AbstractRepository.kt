package io.fantasy0v0.po

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface AbstractRepository<T, ID> : JpaRepository<T, ID>, JpaSpecificationExecutor<T>
