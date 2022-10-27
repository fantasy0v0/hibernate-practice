package io.fantasy0v0

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan
@EnableJpaRepositories
@SpringBootApplication
open class App

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
