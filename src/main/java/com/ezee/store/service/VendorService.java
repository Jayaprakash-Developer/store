package com.ezee.store.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.VendorDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.VendorRepository;

@Service
public class VendorService {
	@Autowired
	private VendorRepository vendorRepository;

	public void addVendor(VendorDTO vendorDto) {
		vendorRepository.addvendor(vendorDto);
	}

	@Cacheable(value = "localVendor")
	public List<VendorDTO> fetchAll() {
		return vendorRepository.fetchAllVendor();
	}

	@Cacheable(value = { "RemoteVendor", "localVendor" }, key = "#id", unless = "#result == null")
	public VendorDTO getById(int id) {
		VendorDTO fetchById = vendorRepository.fetchById(id);
		return fetchById;
	}

	@CacheEvict(value = "localVendor", key = "#id")
	public int delete(int id) {
		VendorDTO vendorDto = vendorRepository.fetchById(id);

		if (vendorDto == null) {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return vendorRepository.delete(id);
	}

	public VendorDTO update(int id, Map<String, Object> vendor) {
		VendorDTO fetchById = vendorRepository.fetchById(id);

		if (fetchById != null) {
			vendor.forEach((key, value) -> {
				switch (key) {
				case "vendorName":
					fetchById.setVendorName((String) value);
					break;
				case "vendoraddress":
					fetchById.setVendoraddress((String) value);
					break;
				case "vendorEmail":
					fetchById.setVendorEmail((String) value);
					break;
				case "vendorPhone":
					fetchById.setVendorPhone((long) value);
					break;
				case "vendorRegistrationData":
					fetchById.setVendorRegistrationData((Date) value);
					break;
				case "vendorStatus":
					fetchById.setVendorStatus((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});
			int updatedRows = vendorRepository.update(fetchById);
			if (updatedRows == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}

		return fetchById;
	}
}
