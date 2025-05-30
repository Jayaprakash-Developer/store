package com.ezee.store.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezee.store.dao.ReturnDetailDAO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.ReturnDetailService;

@Controller
@RequestMapping("/returns/product")
public class ReturnDetailController {
	@Autowired
	private ReturnDetailService returnDetailService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addReturnDetail(@RequestBody ReturnDetailDAO returnDetailDAO) {
		returnDetailService.add(returnDetailDAO);
		return ResponseException.success("Tax Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllReturnDetail() {
		return ResponseException.success(returnDetailService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchReturnDetailById(@PathVariable int id) {
		return ResponseException.success(returnDetailService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteReturnDetail(@PathVariable int id) {
		return ResponseException.success(returnDetailService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateReturnDetail(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(returnDetailService.update(id, tax));
	}
}
