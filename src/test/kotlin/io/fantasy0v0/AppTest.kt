package io.fantasy0v0

import io.fantasy0v0.po.clazz.Clazz
import io.fantasy0v0.po.clazz.ClazzRepository
import io.fantasy0v0.po.student.Student
import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.po.student.Student_
import io.fantasy0v0.uitl.StudentHelper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import javax.transaction.Transactional

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