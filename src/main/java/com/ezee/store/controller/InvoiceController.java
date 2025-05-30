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

import com.ezee.store.dao.InvoiceDAO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.InvoiceService;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {
	@Autowired
	private InvoiceService invoiceService;

	@PostMapping("/new")
	public ResponseEntity<?> addInvoice(@RequestBody InvoiceDAO invoiceDAO) {
		invoiceService.add(invoiceDAO);
		return ResponseException.success("Tax Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> fetchAllInvoice() {
		return ResponseException.success(invoiceService.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchInvoiceById(@PathVariable int id) {
		return ResponseException.success(invoiceService.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteInvoice(@PathVariable int id) {
		return ResponseException.success(invoiceService.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateInvoice(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(invoiceService.update(id, tax));
	}
}
