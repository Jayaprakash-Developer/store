package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.EmployeeDAO;
import com.ezee.store.dto.EmployeeDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	public void addEmployee(EmployeeDAO employeeDao) {
		int addemployee = employeeRepository.addemployee(employeeDao);
		
		if (addemployee == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}
	}

	@Cacheable(value = "localEmployee")
	public List<EmployeeDTO> fetchAll() {
		return employeeRepository.fetchAllCustomer();
	}

	@Cacheable(value = { "remoteEmployee", "localEmployee" }, key = "#id", unless = "#result == null")
	public EmployeeDTO getById(int id) {
		EmployeeDTO fetchById = employeeRepository.fetchById(id);
		return fetchById;
	}

	@CacheEvict(value = "localEmployeeDetail", key = "#id")
	public int delete(int id) {
		EmployeeDTO vendorDto = employeeRepository.fetchById(id);

		if (vendorDto == null) {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return employeeRepository.delete(id);
	}

	public EmployeeDAO update(int id, Map<String, Object> employee) {
		EmployeeDAO fetchById = employeeRepository.fetchDaoById(id);

		if (fetchById != null) {
			employee.forEach((key, value) -> {
				switch (key) {
				case "employeeDetailId":
					fetchById.setEmployeeDetailId((int) value);
					break;
				case "employeeRole":
					fetchById.setEmployeeRole((String) value);
					break;
				case "username":
					fetchById.setUsername((String) value);
					break;
				case "password":
					fetchById.setPassword((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});
			int update = employeeRepository.update(fetchById);
			if (update == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return fetchById;

	}
}
