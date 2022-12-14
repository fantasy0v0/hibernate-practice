package io.fantasy0v0.po.clazz

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
class Clazz(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column(nullable = false)
  var name: String,

  @Column(nullable = false, updatable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()
)