package io.fantasy0v0.helper

import io.fantasy0v0.po.clazz.Clazz
import io.fantasy0v0.po.clazz.ClazzRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ClazzHelper @Autowired constructor (
  private var clazzRepository: ClazzRepository
) {

  fun create(): Clazz {
    val clazz = Clazz(name = "测试班级", createdAt = LocalDateTime.now())
    return clazzRepository.save(clazz)
  }

}