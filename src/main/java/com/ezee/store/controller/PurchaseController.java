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

import com.ezee.store.dao.PurchaseDAO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.PurchaseService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
	@Autowired
	private PurchaseService purchaseService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addPurchase(@RequestBody PurchaseDAO purchaseDAO) {
		purchaseService.add(purchaseDAO);
		return ResponseException.success("Purchase Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllPurchase() {
		return ResponseException.success(purchaseService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchPurchaseById(@PathVariable int id) {
		return ResponseException.success(purchaseService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deletePurchase(@PathVariable int id) {
		return ResponseException.success(purchaseService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updatePurchase(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(purchaseService.update(id, tax));
	}
}
