package io.fantasy0v0.specification

import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.student.Student
import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.po.student.Student_
import io.fantasy0v0.po.student.dto.StudentClassDto
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@SpringBootTest
class SpecificationTest(
  @Autowired private val studentHelper: StudentHelper,
  @Autowired private val studentRepository: StudentRepository,
  @Autowired private val entityManager: EntityManager
) {

  @Test
  fun test() {
    val student = studentHelper.create()
    // 按班级编号查询
    val students = studentRepository.findAll_2(null, student.id)
    studentRepository.findById(student.clazz.id)
    Assertions.assertTrue(students.size == 1)
  }

  @Test
  fun testPredicate() {
    val result = entityManager.createQuery("""
      SELECT 'test'
    """.trimIndent()).singleResult
    Assertions.assertEquals("test", result)

    val student = studentHelper.create()
    val cb = entityManager.criteriaBuilder
    val cq = cb.createQuery(Long::class.java)
    val root = cq.from(Student::class.java)
    cq.select(root[Student_.id])
    cq.where(cb.equal(root[Student_.id], student.id))
    val query = entityManager.createQuery(cq)
    query.firstResult = 0
    query.maxResults = 1
    Assertions.assertEquals(student.id, query.singleResult)
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

}