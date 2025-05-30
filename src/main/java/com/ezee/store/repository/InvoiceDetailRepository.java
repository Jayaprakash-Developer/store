package com.ezee.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.store.config.DatabaseConfiguration;
import com.ezee.store.dao.InvoiceDetailDAO;
import com.ezee.store.dto.InvoiceDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class InvoiceDetailRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<InvoiceDetailDTO> fetchAll() {
		List<InvoiceDetailDTO> list = databaseConfiguration.getList();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "select invoice_detail_id, invoice_id, product_id, quantity, price from invoice_detail";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				InvoiceDetailDTO invoiceDetailDTO = new InvoiceDetailDTO();

				invoiceDetailDTO.setInvoiceDetailId(resultSet.getInt("invoice_detail_id"));
				invoiceDetailDTO.setProductDTO(productRepository.fetchById(resultSet.getInt("product_id")));
				invoiceDetailDTO.setInvoiceDTO(invoiceRepository.fetchInvoiceById(resultSet.getInt("invoice_id")));
				invoiceDetailDTO.setPrice(resultSet.getDouble("price"));
				invoiceDetailDTO.setQuantity(resultSet.getInt("quantity"));

				list.add(invoiceDetailDTO);
			}

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return list;
	}

	public InvoiceDetailDTO fetchById(int id) {
		InvoiceDetailDTO invoiceDetailDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select invoice_detail_id, invoice_id, product_id, quantity, price from invoice_detail where invoice_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					invoiceDetailDTO = new InvoiceDetailDTO();

					invoiceDetailDTO.setInvoiceDetailId(resultSet.getInt("invoice_detail_id"));
					invoiceDetailDTO.setProductDTO(productRepository.fetchById(resultSet.getInt("product_id")));
					invoiceDetailDTO.setInvoiceDTO(invoiceRepository.fetchInvoiceById(resultSet.getInt("invoice_id")));
					invoiceDetailDTO.setPrice(resultSet.getDouble("price"));
					invoiceDetailDTO.setQuantity(resultSet.getInt("quantity"));
					
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return invoiceDetailDTO;
	}
	
	public InvoiceDetailDAO fetchInvoiceDetailDAOById(int id) {
		
		InvoiceDetailDAO invoiceDetailDAO = null;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select invoice_detail_id, invoice_id, product_id, quantity, price from invoice_detail where invoice_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					invoiceDetailDAO = new InvoiceDetailDAO();
					
					invoiceDetailDAO.setInvoiceDetailId(resultSet.getInt("invoice_detail_id"));
					invoiceDetailDAO.setProductId(resultSet.getInt("product_id"));
					invoiceDetailDAO.setInvoiceId(resultSet.getInt("invoice_id"));
					invoiceDetailDAO.setPrice(resultSet.getDouble("price"));
					invoiceDetailDAO.setQuantity(resultSet.getInt("quantity"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return invoiceDetailDAO;
	}

	public int add(InvoiceDetailDAO invoiceDetailDAO) {
		
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into invoice_detail (invoice_id, product_id, quantity, price) values (?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			
			int invoiceId = invoiceDetailDAO.getInvoiceId();
			invoiceRepository.fetchInvoiceById(invoiceId);
			statement.setInt(1, invoiceId);
			
			int productId = invoiceDetailDAO.getProductId();
			productRepository.fetchById(productId);
			statement.setInt(2, productId);
			
			statement.setInt(3, invoiceDetailDAO.getQuantity());
			statement.setDouble(4, invoiceDetailDAO.getPrice());

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
			
			String query = "delete from invoice_detail where invoice_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return affected;
	}

	public int update(InvoiceDetailDAO invoiceDetailDAO) {
		
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "update invoice_detail set invoice_id = ?, product_id = ?, quantity = ?, price = ? where invoice_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
		
			int invoiceId = invoiceDetailDAO.getInvoiceId();
			invoiceRepository.fetchInvoiceById(invoiceId);
			statement.setInt(1, invoiceId);
			
			int productId = invoiceDetailDAO.getProductId();
			productRepository.fetchById(productId);
			statement.setInt(2, productId);
			
			statement.setInt(3, invoiceDetailDAO.getQuantity());
			statement.setDouble(4, invoiceDetailDAO.getPrice());
			statement.setInt(5, invoiceDetailDAO.getInvoiceDetailId());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return executeUpdate;
	}
	
	public int addListOfInvoiceDetail(List<InvoiceDetailDAO> list) {
		int size = list.size();
		int[] affected = new int[size];
		int affectedRow = 1;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into invoice_detail (invoice_id, product_id, quantity, price) values (?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			
			for (Iterator<InvoiceDetailDAO> iterator = list.iterator(); iterator.hasNext();) {
				InvoiceDetailDAO invoiceDetailDAO = iterator.next();
				
				int invoiceId = invoiceDetailDAO.getInvoiceId();
				invoiceRepository.fetchInvoiceById(invoiceId);
				statement.setInt(1, invoiceId);
				
				int productId = invoiceDetailDAO.getProductId();
				productRepository.fetchById(productId);
				statement.setInt(2, productId);
				
				statement.setInt(3, invoiceDetailDAO.getQuantity());
				statement.setDouble(4, invoiceDetailDAO.getPrice());
				
				statement.addBatch();
			}
			
			affected = statement.executeBatch();
			
			if (affected.length != size) {
				affectedRow = 0;
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 
		
		return affectedRow;
	}
}
