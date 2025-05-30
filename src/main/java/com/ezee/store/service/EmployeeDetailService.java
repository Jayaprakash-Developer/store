package com.ezee.store.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.EmployeeDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.EmployeeDetailsRepository;

@Service
public class EmployeeDetailService {
	@Autowired
	private EmployeeDetailsRepository employeeDetailRepo;

	public void addEmployeeDetail(EmployeeDetailDTO employeeDetailDto) {
		int product = employeeDetailRepo.addProduct(employeeDetailDto);
		
		if (product == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}
	}

	@Cacheable(value = "localEmployeeDetail")
	public List<EmployeeDetailDTO> fetchAll() {
		return employeeDetailRepo.fetchAllEmployee();
	}

	@Cacheable(value = { "remoteEmployeeDetail", "localEmployeeDetail" }, key = "#id", unless = "#result == null")
	public EmployeeDetailDTO getById(int id) {
		EmployeeDetailDTO fetchById = employeeDetailRepo.fetchById(id);
		return fetchById;
	}

	@CacheEvict(value = "localEmployeeDetail", key = "#id")
	public int delete(int id) {
		EmployeeDetailDTO vendorDto = employeeDetailRepo.fetchById(id);

		if (vendorDto == null) {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return employeeDetailRepo.delete(id);
	}

	public EmployeeDetailDTO update(int id, Map<String, Object> vendor) {
		EmployeeDetailDTO fetchById = employeeDetailRepo.fetchById(id);

		if (fetchById != null) {
			vendor.forEach((key, value) -> {
				switch (key) {
				case "employeeName":
					fetchById.setEmployeeName((String) value);
					break;
				case "gender":
					fetchById.setGender((String) value);
					break;
				case "dateOfBirth":
					fetchById.setDateOfBirth((Date) value);
					break;
				case "email":
					fetchById.setEmail((String) value);
					break;
				case "phoneNo":
					fetchById.setPhoneNo((String) value);
					break;
				case "hireDate":
					fetchById.setHireDate((Date) value);
					break;
				case "salary":
					fetchById.setSalary((double) value);
					break;
				case "address":
					fetchById.setAddress((String) value);
					break;
				case "status":
					fetchById.setStatus((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});
			int update = employeeDetailRepo.update(fetchById);
			if (update == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return fetchById;

	}

}
