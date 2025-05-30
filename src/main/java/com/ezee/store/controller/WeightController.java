package com.ezee.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.store.dto.WeightDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.service.WeightService;

@RestController
@RequestMapping("/weight")
public class WeightController {
	@Autowired
	private WeightService weightService;

	@PostMapping("/insert")
	public ResponseEntity<?> createUser(@RequestBody WeightDTO weightDTO) {
		ResponseEntity<?> response = null;
		try {
			weightService.saveWeight(weightDTO);
			response = ResponseException.success("Inserted Successfully");
		} catch (ServiceException se) {
			response = ResponseEntity.status(HttpStatus.OK).body(ResponseException.failure(se.getErrorCode()));
		}
		return response;

	}

	@GetMapping("/getall")
	public ResponseEntity<?> findAll() {
		ResponseEntity<?> response = null;
		try {
			List<WeightDTO> allWeight = weightService.getAllWeight();
			response = ResponseException.success(allWeight);
		} catch (ServiceException se) {
			response = ResponseEntity.status(HttpStatus.OK).body(ResponseException.failure(se.getErrorCode()));
		}
		return response;
	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> findById(@PathVariable int id) {
		ResponseEntity<?> response = null;
		try {
			WeightDTO weightById = weightService.getWeightById(id);
			System.out.println("Fetched data");
			response = ResponseException.success(weightById);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateWeight(@PathVariable int id, @RequestBody Map<String, Object> weight) {
		ResponseEntity<?> response = null;
		try {
			weightService.updateWeight(id, weight);
			response = ResponseException.success("Updated Sucessfully");
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;
	}

	@PostMapping("delete/{id}")
	public ResponseEntity<?> deleteWeight(@PathVariable int id) {
		ResponseEntity<?> response = null;
		try {
			int deleteWeight = weightService.deleteWeight(id);
			response = ResponseEntity.ok(deleteWeight);
		} catch (ServiceException se) {
			response = ResponseException.failure(se.getErrorCode());
		}
		return response;

	}
}
