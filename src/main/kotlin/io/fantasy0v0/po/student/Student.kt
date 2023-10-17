package io.fantasy0v0.po.student

import io.fantasy0v0.po.clazz.Clazz
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@DynamicUpdate
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = 0,

  @Column(nullable = false)
  var name: String,

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  var clazz: Clazz,

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  var createdAt: LocalDateTime = LocalDateTime.MIN,

  @UpdateTimestamp
  @Column(nullable = false)
  var updatedAt: LocalDateTime = LocalDateTime.MIN
)