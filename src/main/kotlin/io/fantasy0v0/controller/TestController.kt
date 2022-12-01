package io.fantasy0v0.controller

import io.fantasy0v0.helper.ClazzHelper
import io.fantasy0v0.po.clazz.ClazzRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
open class TestController(
  @Autowired private val clazzHelper: ClazzHelper,
  @Autowired private val clazzRepository: ClazzRepository
) {

  private val log: Logger = LoggerFactory.getLogger(TestController::class.java)

  /**
   * 没有事务的情况下, 不会自动提交对实体类的修改
   */
  @GetMapping("/test")
  open fun test(): String {
    val clazz = clazzHelper.create()
    log.info(String.format("%s %s", clazz.id, clazz.name))
    clazz.name = "modify"
    return String.format("%s %s", clazz.id, clazz.name)
  }

  /**
   * 参考上一个方法, 在没有事务的情况下, 需要手动调用save方法才会提交对entity的修改
   */
  @GetMapping("/test-with-save")
  open fun testWithSave(): String {
    val clazz = clazzHelper.create()
    log.info(String.format("%s %s", clazz.id, clazz.name))
    clazz.name = "modify"
    clazzRepository.save(clazz)
    return String.format("%s %s", clazz.id, clazz.name)
  }

  @GetMapping("/findById")
  open fun findById(@RequestParam id: Long): String {
    val clazz = clazzRepository.findByIdOrNull(id) ?: return "Nothing"
    return "id:${clazz.id} name:${clazz.name}"
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