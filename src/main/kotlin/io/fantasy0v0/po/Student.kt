package io.fantasy0v0.po

import io.fantasy0v0.annotation.NoArg
import jakarta.persistence.*

@NoArg
@Entity
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long,

  @Column(nullable = false)
  var name: String
)