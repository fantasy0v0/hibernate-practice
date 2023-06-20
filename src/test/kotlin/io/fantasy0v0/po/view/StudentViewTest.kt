package io.fantasy0v0.po.view

import io.fantasy0v0.helper.StudentHelper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional


@Transactional
@SpringBootTest
internal class StudentViewTest @Autowired constructor(
  val studentHelper: StudentHelper,
  val studentViewRepository: StudentViewRepository) {

  @Test
  fun testView() {
    val student = studentHelper.create()
    val viewList = studentViewRepository.findAll()
    assertTrue(viewList.size > 0)
    assertTrue(viewList.stream().anyMatch {
      student.id == it.id
    })
  }

}