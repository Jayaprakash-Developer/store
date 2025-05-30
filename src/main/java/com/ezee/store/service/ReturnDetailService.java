package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.ReturnDetailDAO;
import com.ezee.store.dto.ReturnDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.ReturnDetailRepository;

@Service
public class ReturnDetailService {
	@Autowired
	private ReturnDetailRepository returnDetailRepository;

	public ReturnDetailDTO fetchById(int id) { 
		return returnDetailRepository.fetchById(id);
	}

	public List<ReturnDetailDTO> getAll() {
		return returnDetailRepository.fetchAll();
	}

	public int add(ReturnDetailDAO returnDetailDAO) {
		int affected = returnDetailRepository.add(returnDetailDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return affected;
	}

	public ReturnDetailDTO delete(int id) {
		ReturnDetailDTO returnDetailDTO = returnDetailRepository.fetchById(id);

		int affected = returnDetailRepository.delete(id);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return returnDetailDTO;
	}

	public ReturnDetailDTO update(int id, Map<String, Object> purchase) {
		ReturnDetailDAO returnDetailDAO = returnDetailRepository.fetchReturnDetailDAO(id);

		if (returnDetailDAO != null) {
			purchase.forEach((key, value) -> {
				switch (key) {
				case "returnProductId":
					returnDetailDAO.setReturnProductId((int) value);
					break;
				case "productId":
					returnDetailDAO.setProductId((int) value);
					break;
				case "quantity":
					returnDetailDAO.setQuantity((int) value);
					break;
				case "price":
					returnDetailDAO.setPrice((double) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = returnDetailRepository.update(returnDetailDAO);

		if(affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}
		
		ReturnDetailDTO returnDetailDTO = returnDetailRepository.fetchById(id);

		return returnDetailDTO;
	}
}
