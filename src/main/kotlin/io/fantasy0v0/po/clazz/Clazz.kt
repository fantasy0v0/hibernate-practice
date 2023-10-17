package io.fantasy0v0.po.clazz

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@DynamicUpdate
class Clazz(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = 0,

  @Column(nullable = false)
  var name: String,

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  var createdAt: LocalDateTime = LocalDateTime.MIN
)