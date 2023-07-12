package io.fantasy0v0.entityManager

import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.student.StudentRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.format.DateTimeFormatter

@SpringBootTest
class EntityManagerTest(
  @Autowired val entityManager: EntityManager,
  @Autowired private val studentHelper: StudentHelper,
  @Autowired private val studentRepository: StudentRepository
) {

  @Test
  fun test() {
    val student = studentHelper.create()
    val query = entityManager.createNativeQuery(
      """
    select c.name as name, count(s.id) as count from clazz c join student s on c.id = s.clazz_id group by c.name 
    """.trimIndent()
    )
    val result = query.resultList
    for (line in result) {
      val data = line as Array<*>
      assertEquals(student.clazz.name, data[0] as String)
      assertTrue(data[1] as Long > 0)
    }
  }

  @Test
  fun testInterfaceBased() {
    val student = studentHelper.create()
    val result = studentRepository.testInterfaceBased()
    for (data in result) {
      assertEquals(student.clazz.name, data.name)
      assertTrue(data.count > 0)
    }
  }

  @Test
  fun testNativeFunction() {
    val student = studentHelper.create()
    val query = entityManager.createQuery("""
      select s.id from Student s where function('to_char', s.createdAt, 'YYYY-MM-DD HH24:MI:SS') = ?1
    """.trimIndent(), Long::class.java)
    val dateStr = student.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    query.setParameter(1, dateStr)
    val result = query.resultList
    assertEquals(1, result.size)
    assertEquals(student.id, result[0])
  }

}