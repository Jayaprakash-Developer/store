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

import com.ezee.store.dao.ReturnProductDAO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.ReturnProductService;

@Controller
@RequestMapping("/returnproduct/detial")
public class ReturnProductController {

	@Autowired
	private ReturnProductService returnProductService;

	@PostMapping("/add")
	public ResponseEntity<?> addReturnProduct(@RequestBody ReturnProductDAO returnProductDAO) {
		returnProductService.add(returnProductDAO);
		return ResponseException.success("Tax Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllReturnProduct() {
		return ResponseException.success(returnProductService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchReturnProductById(@PathVariable int id) {
		return ResponseException.success(returnProductService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteReturnProduct(@PathVariable int id) {
		return ResponseException.success(returnProductService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateReturnProduct(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(returnProductService.update(id, tax));
	}
}
