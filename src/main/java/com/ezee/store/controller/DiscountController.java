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

import com.ezee.store.dto.DiscountDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.DiscountService;

@Controller
@RequestMapping("/discount")
public class DiscountController {
	
	@Autowired
	private DiscountService discountService;
	
	@PostMapping("/new")
	public ResponseEntity<?> addDiscount(@RequestBody DiscountDTO discountDTO) {
		discountService.add(discountDTO);
		return ResponseException.success("Discount Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllDiscount() {
		return ResponseException.success(discountService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchDiscountById(@PathVariable int id) {
		return ResponseException.success(discountService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteDiscount(@PathVariable int id) {
		return ResponseException.success(discountService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateDiscount(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(discountService.update(id, tax));
	}
}
