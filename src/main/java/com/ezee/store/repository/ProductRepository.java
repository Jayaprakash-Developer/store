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
import com.ezee.store.dao.ProductDAO;
import com.ezee.store.dto.CategoryDTO;
import com.ezee.store.dto.ProductDTO;
import com.ezee.store.dto.WeightDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class ProductRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private WeightRepository weightRepo;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public boolean existsById(int id) {

		boolean exists = false;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "SELECT COUNT(product_id) FROM product_detail WHERE product_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				exists = count > 0;
			}
		} catch (SQLException e) {
			LOGGER.error("Error in existsById: {}/n, data: {}", e.getMessage(), e);
		}

		return exists;
	}

	public List<ProductDTO> fetchAllCustomer() {

		List<ProductDTO> list = databaseConfiguration.getList();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "SELECT product_id, product_name, category_id, weight_id, package_type, brand_name, price, expireday, description, updated_at from product_detail";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				ProductDTO productDto = new ProductDTO();
				productDto.setProductId(resultSet.getInt("product_id"));
				productDto.setProductName(resultSet.getString("product_name"));

				CategoryDTO categoryDto = categoryRepo.fetchById(resultSet.getInt("category_id"));
				productDto.setCategoryId(categoryDto);
				WeightDTO weightDto = weightRepo.fetchById(resultSet.getInt("weight_id"));
				productDto.setWeightId(weightDto);
				productDto.setPackageType(resultSet.getString("package_type"));
				productDto.setBrandName(resultSet.getString("brand_name"));
				productDto.setPrice(resultSet.getDouble("price"));
				productDto.setExpireDay(resultSet.getDate("expireday"));
				productDto.setDescription(resultSet.getString("description"));
				productDto.setUpdatedAt(resultSet.getDate("updated_at"));
				list.add(productDto);
			}
			System.out.println("executed product repo. fetchall");
			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return list;
	}

	public ProductDAO fetchProductDAOById(int id) {

		ProductDAO productDao = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT product_id, product_name, category_id, weight_id, package_type, brand_name, price, expireday, description, updated_at from product_detail where product_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					productDao = new ProductDAO();
					productDao.setProductId(resultSet.getInt("product_id"));
					productDao.setProductName(resultSet.getString("product_name"));
					productDao.setCategoryId(resultSet.getInt("category_id"));
					productDao.setWeightId(resultSet.getInt("weight_id"));
					productDao.setPackageType(resultSet.getString("package_type"));
					productDao.setBrandName(resultSet.getString("brand_name"));
					productDao.setPrice(resultSet.getDouble("price"));
					productDao.setExpireDay(resultSet.getDate("expireday"));
					productDao.setDescription(resultSet.getString("description"));
					productDao.setUpdatedAt(resultSet.getDate("updated_at"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productDao;
	}

	public ProductDTO fetchById(int id) {

		ProductDTO productDto = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT product_id, product_name, category_id, weight_id, package_type, brand_name, price, expireday, description, updated_at from product_detail where product_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					productDto = new ProductDTO();
					productDto.setProductId(resultSet.getInt("product_id"));
					productDto.setProductName(resultSet.getString("product_name"));

					CategoryDTO categoryDto = categoryRepo.fetchById(resultSet.getInt("category_id"));
					productDto.setCategoryId(categoryDto);
					WeightDTO weightDto = weightRepo.fetchById(resultSet.getInt("weight_id"));
					productDto.setWeightId(weightDto);
					productDto.setPackageType(resultSet.getString("package_type"));
					productDto.setBrandName(resultSet.getString("brand_name"));
					productDto.setPrice(resultSet.getDouble("price"));
					productDto.setExpireDay(resultSet.getDate("expireday"));
					productDto.setDescription(resultSet.getString("description"));
					productDto.setUpdatedAt(resultSet.getDate("updated_at"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");
		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return productDto;
	}

	public int delete(int id) {

		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "DELETE FROM product_detail WHERE product_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(ProductDAO productDao) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "UPDATE product_detail SET   product_name=?, category_id=?, weight_id=?, package_type=?, brand_name=?, price=?, expireday=?, description=?, updated_at=? WHERE product_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, productDao.getProductName());
			int categoryId = productDao.getCategoryId();
			categoryRepo.fetchById(categoryId);
			statement.setInt(2, categoryId);
			int weightId = productDao.getWeightId();
			weightRepo.fetchById(weightId);
			statement.setInt(3, weightId);
			statement.setString(4, productDao.getPackageType());
			statement.setString(5, productDao.getBrandName());
			statement.setDouble(6, productDao.getPrice());
			statement.setDate(7, productDao.getExpireDay());
			statement.setString(8, productDao.getDescription());
			statement.setDate(9, productDao.getUpdatedAt());
			statement.setInt(10, productDao.getProductId());
			executeUpdate = statement.executeUpdate();
			System.out.println("update executed.");

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}
		return executeUpdate;

	}

	public int addProduct(ProductDAO productDao) {

		int executeUpdate = 0;

		try {
			if (existsById(productDao.getProductId())) {
				throw new ServiceException(ErrorCode.ID_ALREADY_EXISTS);
			}
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "INSERT INTO product_detail (product_id, product_name, category_id, weight_id, package_type, brand_name, price, expireday, description, updated_at) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, productDao.getProductId());
			statement.setString(2, productDao.getProductName());
			int categoryId = productDao.getCategoryId();
			categoryRepo.fetchById(categoryId);
			statement.setInt(3, categoryId);
			int weightId = productDao.getWeightId();
			weightRepo.fetchById(weightId);
			statement.setInt(4, weightId);
			statement.setString(5, productDao.getPackageType());
			statement.setString(6, productDao.getBrandName());
			statement.setDouble(7, productDao.getPrice());
			statement.setDate(8, productDao.getExpireDay());
			statement.setString(9, productDao.getDescription());
			statement.setDate(10, productDao.getUpdatedAt());
			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.DATABASE_ERROR, e);
		}

		return executeUpdate;
	}

}
