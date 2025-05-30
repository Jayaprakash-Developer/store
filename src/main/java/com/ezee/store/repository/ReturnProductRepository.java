package com.ezee.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.store.config.DatabaseConfiguration;
import com.ezee.store.dao.ReturnProductDAO;
import com.ezee.store.dto.ReturnProductDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class ReturnProductRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<ReturnProductDTO> fetchAll() {
		List<ReturnProductDTO> list = databaseConfiguration.getList();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "select return_product_id, invoice_id, refund_amount, return_method, return_date from return_product";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				ReturnProductDTO returnProductDTO = new ReturnProductDTO();

				returnProductDTO.setReturnProductId(resultSet.getInt("return_product_id"));
				returnProductDTO.setInvoiceDTO(invoiceRepository.fetchInvoiceById(resultSet.getInt("invoice_id")));
				returnProductDTO.setRefundDate(resultSet.getDate("return_date"));
				returnProductDTO.setRefundMethod(resultSet.getString("return_method"));
				returnProductDTO.setRefundAmount(resultSet.getDouble("refund_amount"));

				list.add(returnProductDTO);
			}

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return list;
	}

	public ReturnProductDTO fetchById(int id) {
		ReturnProductDTO returnProductDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select return_product_id, invoice_id, refund_amount, return_method, return_date from return_product where return_product_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					returnProductDTO = new ReturnProductDTO();

					returnProductDTO.setReturnProductId(resultSet.getInt("return_product_id"));
					returnProductDTO.setInvoiceDTO(invoiceRepository.fetchInvoiceById(resultSet.getInt("invoice_id")));
					returnProductDTO.setRefundDate(resultSet.getDate("return_date"));
					returnProductDTO.setRefundMethod(resultSet.getString("return_method"));
					returnProductDTO.setRefundAmount(resultSet.getDouble("refund_amount"));
					
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return returnProductDTO;
	}
	
	public ReturnProductDAO fetchReturnProductDAOById(int id) {
		ReturnProductDAO returnProductDAO = null;
		
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select return_product_id, invoice_id, refund_amount, return_method, return_date from return_product where return_product_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					returnProductDAO = new ReturnProductDAO();
					
					returnProductDAO.setReturnProductId(resultSet.getInt("return_product_id"));
					returnProductDAO.setInvoiceId(resultSet.getInt("invoice_id"));
					returnProductDAO.setRefundDate(resultSet.getDate("return_date"));
					returnProductDAO.setRefundMethod(resultSet.getString("return_method"));
					returnProductDAO.setRefundAmount(resultSet.getDouble("refund_amount"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return returnProductDAO;
	}

	public int add(ReturnProductDAO returnProductDAO) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into return_product (invoice_id, refund_amount, return_method, return_date) values (?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			
			int invoiceId = returnProductDAO.getInvoiceId();
			invoiceRepository.fetchInvoiceById(invoiceId);
			statement.setInt(1, invoiceId);
			
			statement.setDouble(2, returnProductDAO.getRefundAmount());
			statement.setString(3, returnProductDAO.getRefundMethod());
			statement.setDate(4, returnProductDAO.getRefundDate());

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return affected;
	}

	public int delete(int id) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "delete from return_product where return_product_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return affected;
	}

	public int update(ReturnProductDAO returnProductDAO) {
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "update return_product set invoice_id = ?, refund_amount = ?, return_method = ?, return_date = ? where return_product_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
		
			int invoiceId = returnProductDAO.getInvoiceId();
			invoiceRepository.fetchInvoiceById(invoiceId);
			statement.setInt(1, invoiceId);
			
			statement.setDouble(2, returnProductDAO.getRefundAmount());
			statement.setString(3, returnProductDAO.getRefundMethod());
			statement.setDate(4, returnProductDAO.getRefundDate());
			statement.setInt(5, returnProductDAO.getReturnProductId());
			
			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}
		
		return executeUpdate;
	}
}
