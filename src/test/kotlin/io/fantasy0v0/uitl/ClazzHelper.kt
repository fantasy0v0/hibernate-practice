package io.fantasy0v0.uitl

import io.fantasy0v0.po.clazz.Clazz
import io.fantasy0v0.po.clazz.ClazzRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClazzHelper(
  @Autowired var clazzRepository: ClazzRepository
) {

  fun create(): Clazz {
    val clazz = Clazz(name = "测试班级")
    return clazzRepository.save(clazz)
  }

}