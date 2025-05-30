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

import com.ezee.store.dao.EmployeeDAO;
import com.ezee.store.dto.EmployeeDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/insert")
	public ResponseEntity<?> createUser(@RequestBody EmployeeDAO employeeDao) {
		ResponseEntity<?> response = null;
		try {
			employeeService.addEmployee(employeeDao);
			response = ResponseException.success("Inserted Successfully");
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		
		return response;
	}

	@GetMapping("/getall")
	public ResponseEntity<?> findAllEmployee() {
		ResponseEntity<?> response = null;
		try {
			List<EmployeeDTO> fetchAll = employeeService.fetchAll();
			response = ResponseException.success(fetchAll);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		
		return response;
	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> findById(@PathVariable int id) {
		ResponseEntity<?> response = null;
		try {
			EmployeeDTO byId = employeeService.getById(id);
			System.out.println("Fetched data");
			response = ResponseException.success(byId);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		
		return response;
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> product) {
		ResponseEntity<?> response = null;
		try {
			employeeService.update(id, product);
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
			int deleteproduct = employeeService.delete(id);
			response = ResponseEntity.ok(deleteproduct);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}

		return response;
	}
}
