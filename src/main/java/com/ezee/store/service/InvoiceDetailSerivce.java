package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.store.dao.InvoiceDetailDAO;
import com.ezee.store.dto.InvoiceDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.InvoiceDetailRepository;

@Repository
public class InvoiceDetailSerivce {
	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;

	public InvoiceDetailDTO fetchById(int id) {
		return invoiceDetailRepository.fetchById(id);
	}

	public List<InvoiceDetailDTO> getAll() {
		return invoiceDetailRepository.fetchAll();
	}

	public int add(InvoiceDetailDAO invoiceDetailDAO) {
		int affected = invoiceDetailRepository.add(invoiceDetailDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return affected;
	}
	
	public int addListOfInvoiceDetail(List<InvoiceDetailDAO> list) {
		int affected = invoiceDetailRepository.addListOfInvoiceDetail(list);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}
		
		return affected;
	}

	public InvoiceDetailDTO delete(int id) {
		InvoiceDetailDTO invoiceDetailDTO = invoiceDetailRepository.fetchById(id);
		int affected = invoiceDetailRepository.delete(id);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return invoiceDetailDTO;
	}

	public InvoiceDetailDTO update(int id, Map<String, Object> tax) {
		InvoiceDetailDAO invoiceDetailDAO = invoiceDetailRepository.fetchInvoiceDetailDAOById(id);
		
		if (invoiceDetailDAO != null) {
			tax.forEach((key, value) -> {
				switch (key) {
				case "invoiceId":
					invoiceDetailDAO.setInvoiceId((int) value);
					break;
				case "productId":
					invoiceDetailDAO.setProductId((int) value);
					break;
				case "quantity":
					invoiceDetailDAO.setQuantity((int) value);
					break;
				case "price":
					invoiceDetailDAO.setPrice((double) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = invoiceDetailRepository.update(invoiceDetailDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}

		InvoiceDetailDTO invoiceDetailDTO = invoiceDetailRepository.fetchById(id);

		return invoiceDetailDTO;
	}
}
