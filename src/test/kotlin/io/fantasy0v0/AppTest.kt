package io.fantasy0v0

import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.helper.StudentHelper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AppTest(
  @Autowired val studentHelper: StudentHelper,
  @Autowired val studentRepository: StudentRepository
) {

  @Test
  fun noSaveButUpdate() {
    val student = studentHelper.create()
    studentRepository.saveAndFlush(student)
    student.name = "xxx1"
  }
}