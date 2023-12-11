package io.fantasy0v0.lazy

import io.fantasy0v0.helper.StudentHelper
import io.fantasy0v0.po.student.StudentRepository
import org.hibernate.LazyInitializationException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LazyTest @Autowired constructor(
  private val studentHelper: StudentHelper,
  private val studentRepository: StudentRepository
) {

  private val log: Logger = LoggerFactory.getLogger(LazyTest::class.java)

  @Test
  fun getClazzId() {
    val studentId = studentHelper.create().id
    try {
      studentRepository.flush()
      val optional = studentRepository.findById(studentId)
      assertTrue(optional.isPresent)
      val student = optional.get()
      // 只获取id不会触发
      log.debug("clazz id: {}", student.clazz.id)
      // 触发懒加载, 由于没有事物, 所以导致报错
      log.debug("clazz name: {}", student.clazz.name)
      assertTrue(false)
    } catch (e: LazyInitializationException) {
      log.debug("正确的分支", e)
      assertTrue(true)
    } finally {
      studentRepository.deleteById(studentId)
    }
  }

}