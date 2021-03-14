package kr.jwor.hellobluegreen.hello.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.jwor.hellobluegreen.hello.entity.Hello;
import kr.jwor.hellobluegreen.hello.repository.HelloRepo;

@Service
public class HelloService {

	@Autowired
	HelloRepo repo;

	public List<Hello> findAll() {
		List<Hello> result = new ArrayList<Hello>();
		result = (List<Hello>) repo.findAll();
		return result;
	}

}
