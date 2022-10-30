package io.fantasy0v0.many_to_one

import io.fantasy0v0.po.student.StudentRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ManyToOneTest(
  @Autowired
  var studentRepository: StudentRepository
) {

  @Test
  fun test1() {
    // TODO
    // val student = studentRepository.getReferenceById(1L)
    // student.clazz.name
  }

}