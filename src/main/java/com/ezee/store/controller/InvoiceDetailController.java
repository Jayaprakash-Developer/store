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

import com.ezee.store.dao.InvoiceDetailDAO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.service.InvoiceDetailSerivce;

@Controller
@RequestMapping("/invoice/details")
public class InvoiceDetailController {
	@Autowired
	private InvoiceDetailSerivce invoiceDetailSerivce;
	
	@PostMapping("/add")
	public ResponseEntity<?> addInvoiceDetail(@RequestBody InvoiceDetailDAO invoiceDetailDAO) {
		invoiceDetailSerivce.add(invoiceDetailDAO);
		return ResponseException.success("InvoiceDetail Created Successfully.");
	}
	
	@PostMapping("/addgroup")
	public ResponseEntity<?> addListOfInvoiceDetail(@RequestBody List<InvoiceDetailDAO> invoiceDetailDAO) {
		invoiceDetailSerivce.addListOfInvoiceDetail(invoiceDetailDAO);
		return ResponseException.success("InvoiceDetail Created Successfully.");
	}

	@GetMapping("/fetchall")
	public ResponseEntity<?> getAllInvoiceDetail() {
		return ResponseException.success(invoiceDetailSerivce.getAll());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchInvoiceDetailById(@PathVariable int id) {
		return ResponseException.success(invoiceDetailSerivce.fetchById(id));
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteInvoiceDetail(@PathVariable int id) {
		return ResponseException.success(invoiceDetailSerivce.delete(id));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateInvoiceDetail(@RequestParam int id, @RequestBody Map<String, Object> tax) {
		return ResponseException.success(invoiceDetailSerivce.update(id, tax));
	}
}
