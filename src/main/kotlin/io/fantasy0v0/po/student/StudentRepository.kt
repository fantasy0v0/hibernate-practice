package io.fantasy0v0.po.student

import io.fantasy0v0.po.AbstractRepository
import org.springframework.data.jpa.repository.Query

interface StudentRepository: AbstractRepository<Student, Long> {

  /**
   * 根据名称查询
   */
  fun findAllByName(name: String): List<Student>

  /**
   * 根据名称查询
   */
  @Query("""
    select s from Student s where s.name = ?1
  """)
  fun findAllByNameWithQuery(name: String): List<Student>

  /**
   * 根据名称查询, 使用原生sql语句
   */
  @Query("""
    select * from student where name = ?1
  """, nativeQuery = true)
  fun findAllByNameWithNativeQuery(name: String): List<Student>
}