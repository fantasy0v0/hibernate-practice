package io.fantasy0v0.po.student1;

import io.fantasy0v0.po.AbstractRepository;

import java.util.Optional;

public interface Student1Repository extends AbstractRepository<Student1, Long> {

  Optional<Student1> findById(long id);

}
