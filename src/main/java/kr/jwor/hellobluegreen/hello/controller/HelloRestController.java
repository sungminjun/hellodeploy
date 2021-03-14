package kr.jwor.hellobluegreen.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jwor.hellobluegreen.hello.entity.Hello;
import kr.jwor.hellobluegreen.hello.repository.HelloRepo;

@RequestMapping("/hello")
@RestController
public class HelloRestController {
	@Autowired
	HelloRepo repo;
	Logger log = LoggerFactory.getLogger(HelloRestController.class);

	@GetMapping("/{id}")
	public ResponseEntity<Hello> readHello(@PathVariable("id") long id) {
		log.info("/hello/ GET");
		
		Hello hello = repo.findById(id).orElse(null);
		if ( hello == null ) {
			return ResponseEntity.badRequest().eTag("no such id").build();
		}
		return ResponseEntity.ok(hello);
	}
	
	@PostMapping("/{hello}")
	public ResponseEntity<Hello> addHello(Hello hello) {
		log.info("/hello/ POST");
		Hello target = repo.findById(hello.getId()).orElse(null);
		if ( hello.getId() != null ) {
			return ResponseEntity.badRequest().eTag("already exists such id").build();
		}
		repo.save(hello);
		return ResponseEntity.ok(hello);
	}

	@PatchMapping("/{hello}")
	public ResponseEntity<Hello> updateHello(Hello hello) {
		log.info("/hello/ PATCH");
		Hello target = repo.findById(hello.getId()).orElse(null);
		if ( target == null ) {
			return ResponseEntity.badRequest().eTag("no such id").build();
		}
		repo.save(hello);
		return ResponseEntity.ok(hello);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Hello> deleteHello(@PathVariable("id") long id) {
		log.info("/hello/ DELETE");
		Hello hello = repo.findById(id).orElse(null);
		if ( hello == null ) {
			return ResponseEntity.badRequest().eTag("no such id").build();
		}
		repo.deleteById(id);
		return ResponseEntity.ok(hello);
	}
}
