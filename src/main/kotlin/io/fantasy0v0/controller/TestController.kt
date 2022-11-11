package io.fantasy0v0.controller

import io.fantasy0v0.helper.ClazzHelper
import io.fantasy0v0.helper.StudentHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
open class TestController(
  @Autowired val clazzHelper: ClazzHelper
) {

  @Transactional
  @GetMapping("/test")
  open fun test(): String {
    val clazz = clazzHelper.create()
    clazz.name = "modify"
    return String.format("%s %s", clazz.id, clazz.name)
  }

}