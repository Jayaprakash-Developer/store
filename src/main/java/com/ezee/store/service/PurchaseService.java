package com.ezee.store.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.PurchaseDAO;
import com.ezee.store.dto.PurchaseDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.PurchaseRepository;

@Service
public class PurchaseService {
	@Autowired
	private PurchaseRepository purchaseRepository;

	public PurchaseDTO fetchById(int id) { 
		return purchaseRepository.fetchById(id);
	}

	public List<PurchaseDTO> getAll() {
		return purchaseRepository.fetchAll();
	}

	public int add(PurchaseDAO purchaseDAO) {
		int affected = purchaseRepository.add(purchaseDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return affected;
	}

	public PurchaseDTO delete(int id) {
		PurchaseDTO purchaseDTO = purchaseRepository.fetchById(id);

		int affected = purchaseRepository.delete(id);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return purchaseDTO;
	}

	public PurchaseDTO update(int id, Map<String, Object> purchase) {
		PurchaseDAO purchaseDAO = purchaseRepository.fetchPurchaseDAOById(id);

		if (purchaseDAO != null) {
			purchase.forEach((key, value) -> {
				switch (key) {
				case "venderId":
					purchaseDAO.setVenderId((int) value);
					break;
				case "taxId":
					purchaseDAO.setTaxId((int) value);
					break;
				case "paymentStatus":
					purchaseDAO.setPaymentStatus((String) value);
					break;
				case "purchaseDate":
					purchaseDAO.setPurchaseDate((Date) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = purchaseRepository.update(purchaseDAO);

		if(affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}
		
		PurchaseDTO purchaseDTO = purchaseRepository.fetchById(id);

		return purchaseDTO;
	}
}
