package kr.jwor.hellobluegreen.hello.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.jwor.hellobluegreen.hello.entity.Hello;
import kr.jwor.hellobluegreen.hello.service.HelloService;

@RequestMapping("/hello")
@Controller
public class HelloController {

	@Autowired
	HelloService svc;
	Logger log = LoggerFactory.getLogger(HelloController.class);
	
	@GetMapping("")
	public String showHellos(Model model) {
		List<Hello> hellos = svc.findAll();
		model.addAttribute("hellos", hellos);
		return "hello";
	}
}
