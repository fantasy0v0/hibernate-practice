package io.fantasy0v0.entityManager

import io.fantasy0v0.helper.StudentHelper
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EntityManagerTest(
  @Autowired val entityManager: EntityManager,
  @Autowired private val studentHelper: StudentHelper,
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

}