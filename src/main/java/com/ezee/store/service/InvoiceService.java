package com.ezee.store.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.InvoiceDAO;
import com.ezee.store.dto.InvoiceDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ResponseException;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.InvoiceRepository;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceRepository invoiceRepository;

	public InvoiceDTO fetchById(int id) { 
		return invoiceRepository.fetchInvoiceById(id);
	}

	public  List<InvoiceDTO> getAll() {
		return invoiceRepository.fetchAllInvoice();
	}

	public ResponseEntity<?> add(InvoiceDAO invoiceDAO) {
		int affected = invoiceRepository.addInvoice(invoiceDAO);

		if (affected == 0) {
			throw new ServiceException(ErrorCode.CREATE_FAILED_EXCEPTION);
		}

		return ResponseException.success("Create User Successfully.");
	}

	public InvoiceDTO delete(int id) {
		InvoiceDTO invoiceDTO = invoiceRepository.fetchInvoiceById(id);
		int affected = invoiceRepository.deleteInvoice(id);
		
		if (affected == 0) {
			throw new ServiceException(ErrorCode.DELETE_FAILED_EXCEPTION, "Id: " + id);
		}

		return invoiceDTO;
	}

	public InvoiceDAO update(int id, Map<String, Object> product) {
		InvoiceDAO invoiceDAO = invoiceRepository.fetchInvoiceDAOById(id);

		if (invoiceDAO != null) {
			product.forEach((key, value) -> {
				switch (key) {
				case "customerId":
					invoiceDAO.setCustomerId((int) value);
					break;
				case "employeeId":
					invoiceDAO.setPaymentMethod((String) value);
					break;
				case "taxId":
					invoiceDAO.setTaxId((int) value);
					break;
				case "discountId":
					invoiceDAO.setDiscountId((int) value);
					break;
				case "paymentStatus":
					invoiceDAO.setPaymentStatus((String) value);
					break;
				case "invoiceDate":
					invoiceDAO.setInvoiceDate((Date) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION, "No key found - " + key + ".");
				}
			});
		}

		int affected = invoiceRepository.updateInvoice(invoiceDAO);

		if(affected == 0) {
			throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION, "Error Occured When Updating Invoice.");
		}

		return invoiceDAO;
	}
}
