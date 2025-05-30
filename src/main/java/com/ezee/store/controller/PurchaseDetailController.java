package com.ezee.store.controller;

import java.util.List;
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

import com.ezee.store.dao.PurchaseDetailDAO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.PurchaseDetailService;

@Controller
@RequestMapping("/purchase/details")
public class PurchaseDetailController {
	@Autowired
	private PurchaseDetailService purchaseDetailService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addPurchaseDetail(@RequestBody PurchaseDetailDAO purchaseDetailDAO) {
		purchaseDetailService.add(purchaseDetailDAO);
		return ResponseException.success("PurchaseDetail Created Successfully.");
	}
	
	@PostMapping("/addgroup")
	public ResponseEntity<?> addListOfPurchaseDetail(@RequestBody List<PurchaseDetailDAO> purchaseDetailDAO) {
		purchaseDetailService.addListOfPurchaseDetail(purchaseDetailDAO);
		return ResponseException.success("PurchaseDetails Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllPurchaseDetail() {
		return ResponseException.success(purchaseDetailService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchPurchaseDetailById(@PathVariable int id) {
		return ResponseException.success(purchaseDetailService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deletePurchaseDetail(@PathVariable int id) {
		return ResponseException.success(purchaseDetailService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updatePurchaseDetail(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(purchaseDetailService.update(id, tax));
	}
}
