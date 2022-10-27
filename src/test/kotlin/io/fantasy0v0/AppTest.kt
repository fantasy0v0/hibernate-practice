package io.fantasy0v0

import io.fantasy0v0.po.Student
import org.junit.jupiter.api.Test

/**
 * Unit test for simple App.
 */
class AppTest {

  @Test
  fun test() {
    println("测试")

    val student = Student(1, "123")
    println(student.toString())
  }
}