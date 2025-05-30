package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.CustomerDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	public void addCustomer(CustomerDTO customerDTO) {
		customerRepo.addCustomer(customerDTO);
	}

	@Cacheable(value = "localCustomers")
	public List<CustomerDTO> fetchAll() {
		return customerRepo.fetchAllCustomer();
	}

	@Cacheable(value = "localCustomers", key = "#id", unless = "#result == null")
	public CustomerDTO getById(int id) {
		CustomerDTO fetchById = customerRepo.getbyId(id);
		return fetchById;
	}

	@CacheEvict(value = "localCustomers", key = "#id")
	public int delete(int id) {
		CustomerDTO customerDTO = customerRepo.getbyId(id);

		if (customerDTO == null) {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return customerRepo.delete(id);

	}

	@CachePut(value = "localCustomers", key = "#id")
	public CustomerDTO update(int id, Map<String, Object> customer) {
		CustomerDTO customerDTO = customerRepo.getbyId(id);

		if (customerDTO != null) {
			customer.forEach((key, value) -> {
				switch (key) {
				case "customerName":
					customerDTO.setCustomerName((String) value);
					break;
				case "address":
					customerDTO.setAddress((String) value);
					break;
				case "email":
					customerDTO.setEmail((String) value);
					break;
				case "phoneNo":
					customerDTO.setPhoneNo((long) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});
			int updatedRows = customerRepo.update(customerDTO);
			if (updatedRows == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return customerDTO;
	}
}
