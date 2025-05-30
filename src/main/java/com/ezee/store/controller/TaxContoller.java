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

import com.ezee.store.dto.TaxDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.TaxService;

@Controller
@RequestMapping("/tax")
public class TaxContoller {
	@Autowired
	private TaxService taxService;
   
	@PostMapping("/add")
	public ResponseEntity<?> addTax(@RequestBody TaxDTO taxDTO) {
		taxService.add(taxDTO);
		return ResponseException.success("Tax Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllTax() {
		return ResponseException.success(taxService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchTaxById(@PathVariable int id) {
		return ResponseException.success(taxService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteTax(@PathVariable int id) {
		return ResponseException.success(taxService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateTax(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(taxService.update(id, tax));
	}
}
