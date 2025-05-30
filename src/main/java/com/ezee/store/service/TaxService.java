package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.TaxDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.TaxRepository;
import com.ezee.store.util.DateTimeUtil;

@Service
public class TaxService {
	@Autowired
	private TaxRepository taxRepository;

	@Cacheable(value = { "taxEhCache" }, key = "#id")
	public TaxDTO fetchById(int id) {
		return taxRepository.fetchTaxById(id);
	}

	public List<TaxDTO> getAll() {
		return taxRepository.fetchAllTax();
	}

	public int add(TaxDTO taxDTO) {

		if (DateTimeUtil.isNull(taxDTO.getTaxName())) {
			throw new ServiceException(ErrorCode.VALUE_NOT_BE_NULL_EXCEPTION, "TaxName not be null or empty.");
		}

		if (taxDTO.getPercentage() == 0) {
			throw new ServiceException(ErrorCode.VALUE_NOT_BE_NULL_EXCEPTION, "Percentage not be 0.");
		}

		int affected = taxRepository.addTax(taxDTO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return affected;
	}

	@CacheEvict(value = "taxEhCache", key = "#id")
	public TaxDTO delete(int id) {
		TaxDTO taxDTO = taxRepository.fetchTaxById(id);
		int affected = taxRepository.deleteTax(id);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return taxDTO;
	}

	@CachePut(value = "taxEhCache", key = "#id")
	public TaxDTO update(int id, Map<String, Object> tax) {
		TaxDTO taxDTO = taxRepository.fetchTaxById(id);

		if (taxDTO != null) {
			tax.forEach((key, value) -> {
				switch (key) {
				case "percentage": {
					if (taxDTO.getPercentage() == 0) {
						throw new ServiceException(ErrorCode.VALUE_NOT_BE_NULL_EXCEPTION, "Percentage not be 0.");
					}

					taxDTO.setPercentage((double) value);
					break;
				}
				case "taxName": {
					if (DateTimeUtil.isNull(taxDTO.getTaxName())) {
						throw new ServiceException(ErrorCode.VALUE_NOT_BE_NULL_EXCEPTION, "TaxName not be null or empty.");
					}
					
					taxDTO.setTaxName((String) value);
					break;
				}
					
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = taxRepository.updateTax(taxDTO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}

		return taxDTO;
	}
}
