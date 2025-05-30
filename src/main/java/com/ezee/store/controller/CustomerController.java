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

import com.ezee.store.dto.CustomerDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/insert")
	public ResponseEntity<?> createUser(@RequestBody CustomerDTO customerDto ){
		ResponseEntity<?> response= null;
		try {
			customerService.addCustomer(customerDto);
		 response=  ResponseException.success("Inserted Successfully");
		}catch (ServiceException se) {
			response=ResponseException.failure(se.getErrorCode());
	    }
		return response;
		
	}
	@GetMapping("/getall")
	public ResponseEntity<?> findAllCustomer(){
		ResponseEntity<?> response= null;
		try {
			List<CustomerDTO> fetchAll = customerService.fetchAll();
			  response = ResponseException.success(fetchAll);
		}catch (ServiceException se) {
	        response = ResponseException.failure(se.getErrorCode());
	    }
		return response;
	}
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> findById(@PathVariable int id) {
		ResponseEntity<?> response= null;
	    try {
	    	CustomerDTO byId = customerService.getById(id);
	        System.out.println(byId);
	        response= ResponseException.success(byId);
       } catch (ServiceException se) {
	    	response= ResponseException.failure(se.getErrorCode());
	    }
		return response;
	    
	}
	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable int id,@RequestBody Map<String, Object> product) {
		ResponseEntity<?> response= null;
		try {
			customerService.update(id, product);
			response= ResponseException.success("Updated Sucessfully");
		}catch (ServiceException se) {
			response= ResponseException.failure(se.getErrorCode());
	    }
		return response;
	}
	 @PostMapping("delete/{id}")
	    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
		 ResponseEntity<?> response= null;
	    	try {
	    		int delete = customerService.delete(id);
	    		response= ResponseEntity.ok(delete);
	    	}catch (ServiceException se) {
	    		response= ResponseException.failure(se.getErrorCode());
		    }
			return response;
	    	
	    }

}
