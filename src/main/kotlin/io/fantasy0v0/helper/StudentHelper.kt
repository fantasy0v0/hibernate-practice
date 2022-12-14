package io.fantasy0v0.helper

import io.fantasy0v0.po.clazz.Clazz
import io.fantasy0v0.po.student.Student
import io.fantasy0v0.po.student.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StudentHelper(
  @Autowired private val clazzHelper: ClazzHelper,
  @Autowired private val studentRepository: StudentRepository) {

  fun create(clazz: Clazz? = null, name: String = "xxx"): Student {
    val student = Student(name = name, clazz = clazz ?: clazzHelper.create(), createdAt = LocalDateTime.now())
    return studentRepository.save(student)
  }

}