package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.CategoryDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepo;

	public void saveCategory(CategoryDTO categoryDto) {
		categoryRepo.addCategory(categoryDto);
	}

	public List<CategoryDTO> getAllCategory() {
		return categoryRepo.fetchAllCustomer();
	}

	@Cacheable(value = { "remoteCategory", "localCategory" }, key = "#id", unless = "#result == null")
	public CategoryDTO getCategoryById(int id) {
		return categoryRepo.fetchById(id);
	}

	@CachePut(value = "localCategory", key = "#id")
	public CategoryDTO updateCategory(int id, Map<String, Object> category) {
		CategoryDTO categoryDto = categoryRepo.fetchById(id);
		if (category != null) {
			category.forEach((key, value) -> {
				switch (key) {
				case "categoryName":
					categoryDto.setCategoryName((String) value);
					break;
				case "categoryDescription":
					categoryDto.setCategoryDescription((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});
			int updatedRows = categoryRepo.update(categoryDto);
			if (updatedRows == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}
		return categoryDto;
	}

	@CacheEvict(value = "localCategory", key = "#id")
	public int deleteCategory(int id) {
		return categoryRepo.delete(id);
	}

}
