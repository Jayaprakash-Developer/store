package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.DiscountDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.DiscountRepository;

@Service
public class DiscountService {
	@Autowired
	private DiscountRepository discountRepository;

	@Cacheable(value = { "discountEhCache" }, key = "#id")
	public DiscountDTO fetchById(int id) { 
		return discountRepository.fetchDiscountById(id);
	}

	public  List<DiscountDTO> getAll() {
		return discountRepository.fetchAllDiscount();
	}

	public int add(DiscountDTO discountDTO) {
		int affected = discountRepository.addDiscount(discountDTO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION, "discountName, percentage can must present");
		}

		return affected;
	}

	@CacheEvict(value = "discountEhCache", key = "#id")
	public DiscountDTO delete(int id) {
		DiscountDTO discountDTO = discountRepository.fetchDiscountById(id);
		int affected = discountRepository.deleteDiscount(id);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return discountDTO;
	}

	@CachePut(value = "discountEhCache", key = "#id")
	public DiscountDTO update(int id, Map<String, Object> discount) {
		DiscountDTO discountDTO = discountRepository.fetchDiscountById(id);

		if (discountDTO != null) {
			discount.forEach((key, value) -> {
				switch (key) {
				case "percentage":
					discountDTO.setPercentage((double) value);
					break;
				case "discountName":
					discountDTO.setDiscountName((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = discountRepository.updateDiscount(discountDTO);

		if(affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}

		return discountDTO;
	}
}
