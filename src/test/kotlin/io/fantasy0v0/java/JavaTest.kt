package io.fantasy0v0.java

import io.fantasy0v0.po.clazz1.Clazz1Repository
import io.fantasy0v0.po.student1.Student1Repository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JavaTest @Autowired constructor (
  private val clazz1Repository: Clazz1Repository,
  private val student1Repository: Student1Repository
) {

  @Test
  fun test1() {
    val clazz1s = clazz1Repository.findAll()
    Assertions.assertTrue(clazz1s.isNotEmpty())
  }

  @Test
  fun test2() {
    val student1s = student1Repository.findAll()
    Assertions.assertTrue(student1s.isNotEmpty())
  }

}