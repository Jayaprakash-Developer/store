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
import com.ezee.store.dao.ReturnDetailDAO;
import com.ezee.store.dto.ReturnDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class ReturnDetailRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private ReturnProductRepository returnProductRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<ReturnDetailDTO> fetchAll() {
		List<ReturnDetailDTO> list = databaseConfiguration.getList();
		
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "select return_detail_id, return_product_id, product_id, quantity, price from return_detail";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				ReturnDetailDTO returnDetailDTO = new ReturnDetailDTO();

				returnDetailDTO.setPrice(resultSet.getDouble("price"));
				returnDetailDTO.setQuantity(resultSet.getInt("quantity"));
				returnDetailDTO.setProductDTO(productRepository.fetchById(resultSet.getInt("product_id")));
				returnDetailDTO.setReturnProductDTO(returnProductRepository.fetchById(resultSet.getInt("return_product_id")));
				returnDetailDTO.setReturnDetailId(resultSet.getInt("return_detail_id"));

				list.add(returnDetailDTO);
			}

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return list;
	}

	public ReturnDetailDTO fetchById(int id) {
		
		ReturnDetailDTO returnDetailDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select return_detail_id, return_product_id, product_id, quantity, price from return_detail where return_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					returnDetailDTO = new ReturnDetailDTO();

					returnDetailDTO.setPrice(resultSet.getDouble("price"));
					returnDetailDTO.setQuantity(resultSet.getInt("quantity"));
					returnDetailDTO.setProductDTO(productRepository.fetchById(resultSet.getInt("product_id")));
					returnDetailDTO.setReturnProductDTO(returnProductRepository.fetchById(resultSet.getInt("return_product_id")));
					returnDetailDTO.setReturnDetailId(resultSet.getInt("return_detail_id"));
					
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return returnDetailDTO;
	}
	
	public ReturnDetailDAO fetchReturnDetailDAO(int id) {
		
		ReturnDetailDAO returnDetailDAO = null;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select return_detail_id, return_product_id, product_id, quantity, price from return_detail where return_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					returnDetailDAO = new ReturnDetailDAO();
					
					returnDetailDAO.setPrice(resultSet.getDouble("price"));
					returnDetailDAO.setQuantity(resultSet.getInt("quantity"));
					returnDetailDAO.setProductId(resultSet.getInt("product_id"));
					returnDetailDAO.setReturnProductId(resultSet.getInt("return_product_id"));
					returnDetailDAO.setReturnDetailId(resultSet.getInt("return_detail_id"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return returnDetailDAO;
	}

	public int add(ReturnDetailDAO returnDetailDAO) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into return_detail (return_product_id, product_id, quantity, price) values (?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			
			int returnProductId = returnDetailDAO.getReturnProductId();
			returnProductRepository.fetchById(returnProductId);
			statement.setInt(1, returnProductId);
			
			int productId = returnDetailDAO.getProductId();
			productRepository.fetchById(productId);
			statement.setInt(2, productId);
			
			statement.setInt(3, returnDetailDAO.getQuantity());
			statement.setDouble(4, returnDetailDAO.getPrice());

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
			
			String query = "delete from return_detail where return_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return affected;
	}

	public int update(ReturnDetailDAO returnDetailDAO) {
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "update return_detail set return_product_id = ?, product_id = ?, quantity = ?, price = ? where return_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
		
			int returnProductId = returnDetailDAO.getReturnProductId();
			returnProductRepository.fetchById(returnProductId);
			statement.setInt(1, returnProductId);
			
			int productId = returnDetailDAO.getProductId();
			productRepository.fetchById(productId);
			statement.setInt(2, productId);
			
			statement.setInt(3, returnDetailDAO.getQuantity());
			statement.setDouble(4, returnDetailDAO.getPrice());
			statement.setInt(5, returnDetailDAO.getReturnDetailId());
			
			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 
		
		return executeUpdate;
	}
}
