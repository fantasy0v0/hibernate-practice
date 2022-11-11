package io.fantasy0v0.controller

import io.fantasy0v0.helper.ClazzHelper
import io.fantasy0v0.helper.StudentHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
open class TestController(
  @Autowired val clazzHelper: ClazzHelper,
  @Autowired val studentHelper: StudentHelper
) {

  @GetMapping("/test")
  fun test(): String {
    val clazz = clazzHelper.create()
    clazz.name = "modify"
    val student = studentHelper.create(clazz)
    return String.format("%s %s", student.id, student.name)
  }

}