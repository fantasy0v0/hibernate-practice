package io.fantasy0v0.many_to_one

import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.po.student.Student_
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.domain.Specification

@SpringBootTest
class ManyToOneTest @Autowired constructor(
  private val studentHelper: StudentHelper,
  private val studentRepository: StudentRepository
) {

  /**
   * 多表关联
   */
  @Test
  fun test1() {
    val student = studentHelper.create()
    println(student.toString())
    println(student.id)
    println("${student.clazz.id}:${student.clazz.name}")
  }

  /**
   * 集合查询
   */
  @Test
  fun test2() {
    studentHelper.create(name = "xxx")
    val spec = Specification { root, _, cb ->
      cb.equal(root[Student_.name], "xxx")
    }
    val students = studentRepository.findAll(spec)
    Assertions.assertTrue(students.isNotEmpty())
  }

}