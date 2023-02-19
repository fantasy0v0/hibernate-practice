package io.fantasy0v0.specification

import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.student.Student
import io.fantasy0v0.po.student.StudentRepository
import io.fantasy0v0.po.student.Student_
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

@Transactional
@SpringBootTest
class SpecificationTest(
  @Autowired private val studentHelper: StudentHelper,
  @Autowired private val studentRepository: StudentRepository
) {

  @Test
  fun test() {
    val student = studentHelper.create()
    // 按班级编号查询
    val students = studentRepository.findAll_2(null, student.id)
    studentRepository.findById(student.clazz.id)
    Assertions.assertTrue(students.size == 1)
  }

}