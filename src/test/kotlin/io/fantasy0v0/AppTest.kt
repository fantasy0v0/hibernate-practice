package io.fantasy0v0

import io.fantasy0v0.po.student.Student
import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.po.student.Student_
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class AppTest(
  @Autowired val studentRepository: StudentRepository
) {

  @Test
  fun test() {
    val student = Student(name = "xxx")
    println(student.toString())
    studentRepository.save(student)
    println(student.id)

    val spec = Specification { root, _, cb ->
      cb.equal(root[Student_.name], "xxx")
    }
    val students = studentRepository.findAll(spec)
    assertTrue(students.isNotEmpty())
  }
}