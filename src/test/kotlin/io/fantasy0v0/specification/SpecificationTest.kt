package io.fantasy0v0.specification

import io.fantasy0v0.helper.ClazzHelper
import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.clazz.Clazz
import io.fantasy0v0.po.clazz.Clazz_
import io.fantasy0v0.po.student.Student
import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.po.student.Student_
import io.fantasy0v0.po.student.dto.StudentClassDto
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@Transactional
@SpringBootTest
class SpecificationTest @Autowired constructor(
  private val clazzHelper: ClazzHelper,
  private val studentHelper: StudentHelper,
  private val studentRepository: StudentRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun test() {
    val student = studentHelper.create()
    // 按班级编号查询
    val pageable = PageRequest.of(0, 10)
    val students = studentRepository.findAll_2(null, student.id, pageable)
    studentRepository.findById(student.clazz.id)
    Assertions.assertTrue(students.size == 1)
  }

  class TestPredicateDTO(val studentId: Long, val clazzId: Long)

  @Test
  fun testPredicate() {
    val result = entityManager.createQuery("""
      SELECT 'test'
    """.trimIndent()).singleResult
    assertEquals("test", result)

    val student = studentHelper.create()
    val cb = entityManager.criteriaBuilder
    val cq = cb.createQuery(Long::class.java)
    var root = cq.from(Student::class.java)
    // query specified join fetching,
    // but the owner of the fetched association was not present in the select list
    // root.fetch(Student_.clazz)
    cq.select(root[Student_.id])
    cq.where(cb.equal(root[Student_.id], student.id))
    val query = entityManager.createQuery(cq)
    query.firstResult = 0
    query.maxResults = 1
    assertEquals(student.id, query.singleResult)

    // multiselect
    val cq1 = cb.createQuery(TestPredicateDTO::class.java)
    root = cq1.from(Student::class.java)
    // root.fetch(Student_.clazz)
    cq1.multiselect(
      root[Student_.id].alias("studentId"),
      root[Student_.clazz][Clazz_.id].alias("clazzId")
    )
    cq1.where(cb.equal(root[Student_.id], student.id))
    val query1 = entityManager.createQuery(cq1)
    query1.firstResult = 0
    query1.maxResults = 1
    assertEquals(student.id, query1.singleResult.studentId)
    assertEquals(student.clazz.id, query1.singleResult.clazzId)
  }

  @Test
  fun testDto() {
    val student = studentHelper.create()
    val cb = entityManager.criteriaBuilder
    val cq = cb.createQuery(StudentClassDto::class.java)
    val root = cq.from(Student::class.java)
    cq.multiselect(
      root,
      root[Student_.clazz]
    )
    cq.where(cb.equal(root[Student_.id], student.id))
    val query = entityManager.createQuery(cq)
    query.firstResult = 0
    query.maxResults = 1
    Assertions.assertEquals(student.id, query.singleResult.student.id)
    Assertions.assertEquals(student.clazz.id, query.singleResult.clazz.id)
  }

  @Test
  fun testJpaDto() {
    val student = studentHelper.create()
    val cb = entityManager.criteriaBuilder
    val cq = cb.createQuery(StudentClassDto::class.java)
    val root = cq.from(Student::class.java)
    cq.select(
      cb.construct(
        StudentClassDto::class.java,
        root,
        root[Student_.clazz]
      )
    )
    cq.where(cb.equal(root[Student_.id], student.id))
    val query = entityManager.createQuery(cq)
    query.firstResult = 0
    query.maxResults = 1
    Assertions.assertEquals(student.id, query.singleResult.student.id)
    Assertions.assertEquals(student.clazz.id, query.singleResult.clazz.id)
  }

  @Test
  fun testCustomQuery() {
    var clazz = clazzHelper.create()
    for (i in 1..3) {
      studentHelper.create(clazz)
    }
    clazz = clazzHelper.create()
    for (i in 1..5) {
      studentHelper.create(clazz)
    }
    clazz = clazzHelper.create()
    for (i in 1..7) {
      studentHelper.create(clazz)
    }

    val builder = entityManager.criteriaBuilder
    val criteriaQuery = builder.createQuery(ClassStudentCount::class.java)
    val root = criteriaQuery.from(Clazz::class.java)
    val subQuery = criteriaQuery.subquery(Long::class.java)
    val studentRoot = subQuery.from(Student::class.java)
    subQuery.select(
      builder.count(
        studentRoot[Student_.id]
      )
    )
    val subPredicate = builder.equal(
      studentRoot[Student_.clazz][Clazz_.id],
      root[Clazz_.id]
    )
    subQuery.where(subPredicate)
    criteriaQuery.orderBy(builder.asc(root[Clazz_.id]))
    criteriaQuery.multiselect(
      root,
      subQuery
    )
    val query = entityManager.createQuery(criteriaQuery)
    assertEquals(3, query.resultList[0].count)
    assertEquals(5, query.resultList[1].count)
    assertEquals(7, query.resultList[2].count)

    val query1 = entityManager.createQuery("""
      select
        new io.fantasy0v0.specification.ClassStudentCount(
          c,
          (select count(1) from Student where clazz = c)    
        )
      from Clazz c order by c.id asc
    """.trimIndent())
    val resultList1 = query1.resultList as List<ClassStudentCount>
    assertEquals(3, resultList1[0].count)
    assertEquals(5, resultList1[1].count)
    assertEquals(7, resultList1[2].count)
  }

}