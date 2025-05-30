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
import com.ezee.store.dao.PurchaseDetailDAO;
import com.ezee.store.dto.PurchaseDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class PurchaseDetailRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<PurchaseDetailDTO> fetchAll() {
		List<PurchaseDetailDTO> list = databaseConfiguration.getList();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "select purchase_detail_id, purchase_id, product_id, purchase_quantity, purchase_price from purchase_detail";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				PurchaseDetailDTO purchaseDetailDTO = new PurchaseDetailDTO();

				purchaseDetailDTO.setPrice(resultSet.getDouble("purchase_price"));
				purchaseDetailDTO.setPurchaseDetailId(resultSet.getInt("purchase_detail_id"));
				purchaseDetailDTO.setPurchaseDTO(purchaseRepository.fetchById(resultSet.getInt("purchase_id")));
				purchaseDetailDTO.setProductDTO(productRepository.fetchById(resultSet.getInt("product_id")));
				purchaseDetailDTO.setQuantity(resultSet.getInt("purchase_quantity"));

				list.add(purchaseDetailDTO);
			}

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return list;
	}

	public PurchaseDetailDTO fetchById(int id) {
		PurchaseDetailDTO purchaseDetailDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select purchase_detail_id, purchase_id, product_id, purchase_quantity, purchase_price from purchase_detail where purchase_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					purchaseDetailDTO = new PurchaseDetailDTO();

					purchaseDetailDTO.setPrice(resultSet.getDouble("purchase_price"));
					purchaseDetailDTO.setPurchaseDetailId(resultSet.getInt("purchase_detail_id"));
					purchaseDetailDTO.setPurchaseDTO(purchaseRepository.fetchById(resultSet.getInt("purchase_id")));
					purchaseDetailDTO.setProductDTO(productRepository.fetchById(resultSet.getInt("product_id")));
					purchaseDetailDTO.setQuantity(resultSet.getInt("purchase_quantity"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return purchaseDetailDTO;
	}

	public PurchaseDetailDAO fetchPurchaseDetailDAO(int id) {
		PurchaseDetailDAO purchaseDetailDAO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select purchase_detail_id, purchase_id, product_id, purchase_quantity, purchase_price from purchase_detail where purchase_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					purchaseDetailDAO = new PurchaseDetailDAO();

					purchaseDetailDAO.setPrice(resultSet.getDouble("purchase_price"));
					purchaseDetailDAO.setPurchaseDetailId(resultSet.getInt("purchase_detail_id"));
					purchaseDetailDAO.setPurchaseId(resultSet.getInt("purchase_id"));
					purchaseDetailDAO.setProductId(resultSet.getInt("product_id"));
					purchaseDetailDAO.setQuantity(resultSet.getInt("purchase_quantity"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return purchaseDetailDAO;
	}

	public int add(PurchaseDetailDAO purchaseDetailDAO) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "insert into purchase_detail (purchase_id, product_id, purchase_quantity, purchase_price) values (?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			int purchaseId = purchaseDetailDAO.getPurchaseId();
			purchaseRepository.fetchById(purchaseId);
			statement.setInt(1, purchaseId);

			int productId = purchaseDetailDAO.getProductId();
			productRepository.fetchById(productId);
			statement.setInt(2, productId);

			statement.setInt(3, purchaseDetailDAO.getQuantity());
			statement.setDouble(4, purchaseDetailDAO.getPrice());

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

			String query = "delete from purchase_detail where purchase_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(PurchaseDetailDAO purchaseDetailDAO) {
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "update purchase_detail set purchase_id, product_id, purchase_quantity, purchase_price  = ? where purchase_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			int purchaseId = purchaseDetailDAO.getPurchaseId();
			purchaseRepository.fetchById(purchaseId);
			statement.setInt(1, purchaseId);

			int productId = purchaseDetailDAO.getProductId();
			productRepository.fetchById(productId);
			statement.setInt(2, productId);

			statement.setInt(3, purchaseDetailDAO.getQuantity());
			statement.setDouble(4, purchaseDetailDAO.getPrice());
			statement.setInt(5, purchaseDetailDAO.getPurchaseDetailId());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}

	public int addListOfPurchaseDetail(List<PurchaseDetailDAO> list) {
		int size = list.size();
		int[] affected = new int[size];
		int affectedRow = 1;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into purchase_detail (purchase_id, product_id, purchase_quantity, purchase_price) values (?, ?, ?, ?)";
			
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			for (Iterator<PurchaseDetailDAO> iterator = list.iterator(); iterator.hasNext();) {
				PurchaseDetailDAO purchaseDetailDAO = iterator.next();
				
				int purchaseId = purchaseDetailDAO.getPurchaseId();
				purchaseRepository.fetchById(purchaseId);
				statement.setInt(1, purchaseId);
				
				int productId = purchaseDetailDAO.getProductId();
				productRepository.fetchById(productId);
				statement.setInt(2, productId);
				
				statement.setInt(3, purchaseDetailDAO.getQuantity());
				statement.setDouble(4, purchaseDetailDAO.getPrice());
				
				statement.addBatch();
			}

			affected = statement.executeBatch();
			
			int length = affected.length;
			
			if (length != size) {
				affectedRow = 0;				
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return affectedRow;
	}
}
