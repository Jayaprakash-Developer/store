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
import com.ezee.store.dao.PurchaseDAO;
import com.ezee.store.dto.PurchaseDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class PurchaseRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private TaxRepository taxRepository;
	
	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<PurchaseDTO> fetchAll() {
		List<PurchaseDTO> list = databaseConfiguration.getList();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "select purchase_id, vendor_id, tax_id, payment_status, purchase_date from purchase";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				PurchaseDTO purchaseDTO = new PurchaseDTO();

				purchaseDTO.setPurchaseId(resultSet.getInt("purchase_id"));
				purchaseDTO.setVendorDTO(vendorRepository.fetchById(resultSet.getInt("vender_id")));
				purchaseDTO.setTaxDTO(taxRepository.fetchTaxById(resultSet.getInt("tax_id")));
				purchaseDTO.setPurchaseDate(resultSet.getDate("purchase_date"));
				purchaseDTO.setPaymentStatus(resultSet.getString("payment_status"));

				list.add(purchaseDTO);
			}

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return list;
	}

	public PurchaseDTO fetchById(int id) {
		PurchaseDTO purchaseDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select purchase_id, vendor_id, tax_id, payment_status, purchase_date from purchase where purchase_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					purchaseDTO = new PurchaseDTO();

					purchaseDTO.setPurchaseId(resultSet.getInt("purchase_id"));
					purchaseDTO.setVendorDTO(vendorRepository.fetchById(resultSet.getInt("vender_id")));
					purchaseDTO.setTaxDTO(taxRepository.fetchTaxById(resultSet.getInt("tax_id")));
					purchaseDTO.setPurchaseDate(resultSet.getDate("purchase_date"));
					purchaseDTO.setPaymentStatus(resultSet.getString("payment_status"));
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return purchaseDTO;
	}

	public PurchaseDAO fetchPurchaseDAOById(int id) {
		PurchaseDAO purchaseDAO = null;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select purchase_id, vendor_id, tax_id, payment_status, purchase_date from purchase where purchase_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					purchaseDAO = new PurchaseDAO();

					purchaseDAO.setPurchaseId(resultSet.getInt("purchase_id"));
					purchaseDAO.setVenderId(resultSet.getInt("vender_id"));
					purchaseDAO.setTaxId(resultSet.getInt("tax_id"));
					purchaseDAO.setPurchaseDate(resultSet.getDate("purchase_date"));
					purchaseDAO.setPaymentStatus(resultSet.getString("payment_status"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return purchaseDAO;
	}

	public int add(PurchaseDAO purchaseDAO) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "insert into purchase (vendor_id, tax_id, payment_status, purchase_date) values (?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			int venderId = purchaseDAO.getVenderId();
			vendorRepository.fetchById(venderId);
			statement.setInt(1, venderId);

			int taxId = purchaseDAO.getTaxId();
			taxRepository.fetchTaxById(taxId);
			statement.setInt(2, taxId);

			statement.setString(3, purchaseDAO.getPaymentStatus());
			statement.setDate(4, purchaseDAO.getPurchaseDate());

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

			String query = "delete from purchase where purchase_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(PurchaseDAO purchaseDAO) {
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "update purchase set vendor_id = ?, tax_id = ?, payment_status =?, purchase_date = ? where purchase_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			int venderId = purchaseDAO.getVenderId();
			vendorRepository.fetchById(venderId);
			statement.setInt(1, venderId);

			int taxId = purchaseDAO.getTaxId();
			taxRepository.fetchTaxById(taxId);
			statement.setInt(2, taxId);

			statement.setString(3, purchaseDAO.getPaymentStatus());
			statement.setDate(4, purchaseDAO.getPurchaseDate());
			statement.setInt(5, purchaseDAO.getPurchaseId());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return executeUpdate;

	}
}
