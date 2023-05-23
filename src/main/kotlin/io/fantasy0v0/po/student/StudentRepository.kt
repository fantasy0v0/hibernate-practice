package io.fantasy0v0.po.student

import io.fantasy0v0.po.AbstractRepository
import io.fantasy0v0.po.student.dto.StudentClassDto
import org.springframework.data.domain.Pageable
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

  @Query("""
    select 
    new io.fantasy0v0.po.student.dto.StudentClassDto(s, c)
    from Student s left join Clazz c on c = s.clazz
  """)
  fun findAll_1(): List<StudentClassDto>

  @Query(
    """
    Select s from Student s left join fetch Clazz c on c = s.clazz
    Where
      (?1 is null or c.id = ?1)
    and
      (?2 is null or s.id = ?2)
  """
  )
  fun findAll_2(clazzId: Long?, studentId: Long?, pageable: Pageable): List<Student>

  @Query(
    """select
    c.name as name, count(s.id) as count
  from clazz c join student s on c.id = s.clazz_id
  group by c.name""", nativeQuery = true
  )
  fun testInterfaceBased(): List<InterfaceBased>

}