package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.PurchaseDetailDAO;
import com.ezee.store.dto.PurchaseDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.PurchaseDetailRepository;

@Service
public class PurchaseDetailService {
	@Autowired
	private PurchaseDetailRepository purchaseDetailRepository;

	public PurchaseDetailDTO fetchById(int id) { 
		return purchaseDetailRepository.fetchById(id);
	}

	public List<PurchaseDetailDTO> getAll() {
		return purchaseDetailRepository.fetchAll();
	}

	public int add(PurchaseDetailDAO purchaseDetailDAO) {
		int affected = purchaseDetailRepository.add(purchaseDetailDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return affected;
	}
	
	public int addListOfPurchaseDetail(List<PurchaseDetailDAO> purchaseDetailDAO) {
		int affected = purchaseDetailRepository.addListOfPurchaseDetail(purchaseDetailDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION, "length mismatch");
		}

		return affected;
	}

	public PurchaseDetailDTO delete(int id) {
		PurchaseDetailDTO purchaseDetailDTO = purchaseDetailRepository.fetchById(id);

		int affected = purchaseDetailRepository.delete(id);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return purchaseDetailDTO;
	}

	public PurchaseDetailDTO update(int id, Map<String, Object> purchase) {
		PurchaseDetailDAO purchaseDetailDAO = purchaseDetailRepository.fetchPurchaseDetailDAO(id);

		if (purchaseDetailDAO != null) {
			purchase.forEach((key, value) -> {
				switch (key) {
				case "purchaseId":
					purchaseDetailDAO.setPurchaseId((int) value);
					break;
				case "productId":
					purchaseDetailDAO.setProductId((int) value);
					break;
				case "quantity":
					purchaseDetailDAO.setQuantity((int) value);
					break;
				case "price":
					purchaseDetailDAO.setPrice((double) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = purchaseDetailRepository.update(purchaseDetailDAO);

		if(affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}

		PurchaseDetailDTO purchaseDetailDTO = purchaseDetailRepository.fetchById(id);

		return purchaseDetailDTO;
	}
}
