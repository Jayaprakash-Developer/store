package com.ezee.store.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.ReturnProductDAO;
import com.ezee.store.dto.ReturnProductDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.ReturnProductRepository;

@Service
public class ReturnProductService {
	@Autowired
	private ReturnProductRepository returnProductRepository;

	public ReturnProductDTO fetchById(int id) { 
		return returnProductRepository.fetchById(id);
	}

	public List<ReturnProductDTO> getAll() {
		return returnProductRepository.fetchAll();
	}

	public int add(ReturnProductDAO returnProductDAO) {
		int affected = returnProductRepository.add(returnProductDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return affected;
	}

	public ReturnProductDTO delete(int id) {
		ReturnProductDTO returnProductDTO = returnProductRepository.fetchById(id);

		int affected = returnProductRepository.delete(id);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return returnProductDTO;
	}

	public ReturnProductDTO update(int id, Map<String, Object> purchase) {
		ReturnProductDAO returnProductDAO = returnProductRepository.fetchReturnProductDAOById(id);

		if (returnProductDAO != null) {
			purchase.forEach((key, value) -> {
				switch (key) {
				case "invoiceId":
					returnProductDAO.setInvoiceId((int) value);
					break;
				case "refundAmount":
					returnProductDAO.setRefundAmount((double) value);
					break;
				case "refundMethod":
					returnProductDAO.setRefundMethod((String) value);
					break;
				case "refundDate":
					returnProductDAO.setRefundDate((Date) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ". Keys are: invoiceId, refundAmount, refundMethod, refundDate");
				}
			});
		}

		int affected = returnProductRepository.update(returnProductDAO);

		if(affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}
		
		ReturnProductDTO returnProductDTO = returnProductRepository.fetchById(id);

		return returnProductDTO;
	}
}
