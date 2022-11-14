package io.fantasy0v0.controller

import io.fantasy0v0.helper.ClazzHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
open class TestController(
  @Autowired private val clazzHelper: ClazzHelper
) {

  /**
   * 没有事务的情况下, 不会提交对实体类的修改
   */
  @GetMapping("/test")
  open fun test(): String {
    val clazz = clazzHelper.create()
    clazz.name = "modify"
    return String.format("%s %s", clazz.id, clazz.name)
  }

  /**
   * 有事务的情况下, 会提交对实体类的任何修改
   */
  @Transactional
  @GetMapping("/test-with-transactional")
  open fun testWithTransactional(): String {
    val clazz = clazzHelper.create()
    clazz.name = "modify"
    return String.format("%s %s", clazz.id, clazz.name)
  }

}