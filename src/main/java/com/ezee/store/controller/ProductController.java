package com.ezee.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.store.dao.ProductDAO;
import com.ezee.store.dto.ProductDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService peoductService;

	@PostMapping("/insert")
	public ResponseEntity<?> createUser(@RequestBody ProductDAO productDao) {
		ResponseEntity<?> response = null;
		try {
			peoductService.saveProduct(productDao);
			response = ResponseException.success("Inserted Successfully");
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;

	}

	@GetMapping("/getall")
	public ResponseEntity<?> findAllProduct() {
		ResponseEntity<?> response = null;
		try {
			List<ProductDTO> allProduct = peoductService.getAllProduct();
			response = ResponseException.success(allProduct);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;
	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> findById(@PathVariable int id) {
		ResponseEntity<?> response = null;
		try {
			ProductDTO productById = peoductService.getProductById(id);
			System.out.println("Fetched data");
			response = ResponseException.success(productById);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;

	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> product) {
		ResponseEntity<?> response = null;
		try {
			peoductService.updateProduct(id, product);
			response = ResponseException.success("Updated Sucessfully");
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;
	}

	@PostMapping("delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable int id) {
		ResponseEntity<?> response = null;
		try {
			int deleteproduct = peoductService.deleteproduct(id);
			response = ResponseEntity.ok(deleteproduct);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;

	}

}
