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

import com.ezee.store.dto.EmployeeDetailDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.service.EmployeeDetailService;

@RestController
@RequestMapping("/employeedetail")
public class EmployeeDetailController {
	@Autowired
	private EmployeeDetailService employeeDetailService;
		@PostMapping("/insert")
		public ResponseEntity<?> createUser(@RequestBody EmployeeDetailDTO employeeDetailDto ){
			ResponseEntity<?> response= null;
			try {
				employeeDetailService.addEmployeeDetail(employeeDetailDto);
			 response=  ResponseException.success("Inserted Successfully");
			}catch (ServiceException se) {
				response=ResponseException.failure(se.getErrorCode());
		    }
			return response;
			
		}
		@GetMapping("/getall")
		public ResponseEntity<?> findAllEmployeeDetails(){
			ResponseEntity<?> response= null;
			try {
			 List<EmployeeDetailDTO> fetchAll = employeeDetailService.fetchAll();
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
		 EmployeeDetailDTO byId = employeeDetailService.getById(id);
		        System.out.println(byId);
		        response= ResponseException.success(byId);
	       } catch (ServiceException se) {
		    	response= ResponseException.failure(se.getErrorCode());
		    }
			return response;
		    
		}
		@PostMapping("/update/{id}")
		public ResponseEntity<?> updatevendor(@PathVariable int id,@RequestBody Map<String, Object> employeeDetail) {
			ResponseEntity<?> response= null;
			try {
				employeeDetailService.update(id, employeeDetail);
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
		    		int delete = employeeDetailService.delete(id);
		    		response= ResponseEntity.ok(delete);
		    	}catch (ServiceException se) {
		    		response= ResponseException.failure(se.getErrorCode());
			    }
				return response;
		    	
		    }


}
