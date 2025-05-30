package com.ezee.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.store.dto.CategoryDTO;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/insert")
	public ResponseEntity<?> createUser(@RequestBody CategoryDTO categoryDto ){
		ResponseEntity<?> response= null;
		try {
			categoryService.saveCategory(categoryDto);
			response= ResponseException.success("Inserted Successfully");
		}catch (ServiceException se) {
			response= ResponseException.failure(se.getErrorCode());
	    }
		return response;
		
	}
	@GetMapping("/getall")
	public ResponseEntity<?> findAllCategory(){
		ResponseEntity<?> response= null;
		try {
			List<CategoryDTO> allCategory = categoryService.getAllCategory();
			response= ResponseException.success(allCategory);
		}catch (ServiceException se) {
			response= ResponseException.failure(se.getErrorCode());
	    }
		return response;
	}
		@GetMapping("/getbyid/{id}")
		public ResponseEntity<?> findById(@PathVariable int id) {
			ResponseEntity<?> response= null;
		    try {
		    	CategoryDTO categoryById = categoryService.getCategoryById(id);
		        System.out.println("Fetched data");
		        response= ResponseException.success(categoryById);
		    } catch (ServiceException se) {
				response= ResponseException.failure(se.getErrorCode());
		    }
			return response;
		}
		@PostMapping("/update/{id}")
		public ResponseEntity<?> updateCategory(@PathVariable int id,@RequestBody Map<String, Object> category) {
			ResponseEntity<?> response= null;
			try {
				categoryService.updateCategory(id, category);
				response= ResponseException.success("Updated Sucessfully");
			}catch (ServiceException se) {
				response= ResponseException.failure(se.getErrorCode());
		    }
			return response;
		}
		 @PostMapping("delete/{id}")
		    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
				ResponseEntity<?> response= null;	
			 try {
		        int deleteWeight = categoryService.deleteCategory(id);
		        response= ResponseEntity.ok(deleteWeight);
		    	}catch (ServiceException se) {
					response= ResponseException.failure(se.getErrorCode());
			    }
				return response;
		    }
		 @GetMapping("/rediscached/{id}")
			public ResponseEntity<?> getRedisCachedCustomer(@PathVariable("id") int id) {
				ResponseEntity<?> response= null;
			    Cache cache = cacheManager.getCache("remoteCategory");
			    if (cache != null) {
			        CategoryDTO cachedCategory = cache.get(id, CategoryDTO.class);
			        if (cachedCategory != null) {
			        	response= ResponseException.success(cachedCategory);
			        } else {
			        	response= ResponseEntity.status(200)
			                    .body("Category not found in Redis cache");
			        }
			    } else {
			    	response= ResponseEntity.status(200)
			                .body("Redis cache not configured");
			    }
				return response;
			}

			
			@Autowired
			private CacheManager cacheManager;

			@GetMapping("/ehcached/{id}")
			public ResponseEntity<?> getEhCachedCustomer(@PathVariable("id") int id) {
				ResponseEntity<?> response= null;
			    Cache cache = cacheManager.getCache("localCategory");
			    if (cache != null) {
			    	CategoryDTO cachedCategory = cache.get(id, CategoryDTO.class);
			        if (cachedCategory != null) {
			        	response= ResponseException.success(cachedCategory);
			        } else {
			        	response= ResponseEntity.status(200)
			                .body("Category not found in cache");
			        }
			    } else {
			    	response= ResponseEntity.status(200)
			            .body("Cache not configured");
			    }
				return response;
			}
	

}
