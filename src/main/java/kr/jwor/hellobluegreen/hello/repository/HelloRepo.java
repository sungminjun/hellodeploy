package kr.jwor.hellobluegreen.hello.repository;

import org.springframework.data.repository.CrudRepository;

import kr.jwor.hellobluegreen.hello.entity.Hello;

public interface HelloRepo extends CrudRepository<Hello, Long> {
	// Spring Data JPA will provide CRUD methods.
}
