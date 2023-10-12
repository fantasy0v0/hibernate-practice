package io.fantasy0v0.java

import io.fantasy0v0.helper.ClazzHelper
import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.clazz1.Clazz1Repository
import io.fantasy0v0.po.student1.Student1Repository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@SpringBootTest
class JavaTest @Autowired constructor (
  private val clazz1Repository: Clazz1Repository,
  private val student1Repository: Student1Repository,
  private val clazzHelper: ClazzHelper,
  private val studentHelper: StudentHelper
) {

  @Test
  fun test1() {
    clazzHelper.create()
    val clazz1s = clazz1Repository.findAll()
    Assertions.assertTrue(clazz1s.isNotEmpty())
  }

  @Test
  fun test2() {
    studentHelper.create()
    val student1s = student1Repository.findAll()
    Assertions.assertTrue(student1s.isNotEmpty())
  }

}