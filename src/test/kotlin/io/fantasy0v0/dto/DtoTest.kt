package io.fantasy0v0.dto

import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.student.StudentRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DtoTest(
  @Autowired private val studentRepository: StudentRepository,
  @Autowired private val studentHelper: StudentHelper
  ) {

  @Test
  @Transactional
  fun test_1() {
    studentHelper.create(name = "xxx")
    val dtoList = studentRepository.findAll_1()
    Assertions.assertTrue(dtoList.isNotEmpty())
    val dto = dtoList[0]
    println("stu name:${dto.student.name}")
    println("clazz name:${dto.clazz.name}")
  }
}