package com.ezee.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.store.config.DatabaseConfiguration;
import com.ezee.store.dto.DiscountDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.util.DateTimeUtil;

import lombok.Cleanup;

@Repository
public class DiscountRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<DiscountDTO> fetchAllDiscount() {
		List<DiscountDTO> list = databaseConfiguration.getList();


		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();
			
			String query = "select discount_id, discount_name, discount_percentage, update_at from discount";
			
			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				DiscountDTO discountDTO = new DiscountDTO();
				
				discountDTO.setDiscountId(resultSet.getInt("discount_id"));
				discountDTO.setDiscountName(resultSet.getString("discount_name"));
				discountDTO.setPercentage(resultSet.getDouble("discount_percentage"));
				discountDTO.setUpdatedAt(resultSet.getString("update_at"));
				
				list.add(discountDTO);
			}
			
			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

			resultSet.close();
			
		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(),e);
		} 
		
		return list;
	}

	public DiscountDTO fetchDiscountById(int id) {
		
		DiscountDTO discountDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select discount_id, discount_name, discount_percentage, update_at from discount where discount_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					discountDTO = new DiscountDTO();
					
					discountDTO.setDiscountId(resultSet.getInt("discount_id"));
					discountDTO.setDiscountName(resultSet.getString("discount_name"));
					discountDTO.setPercentage(resultSet.getDouble("discount_percentage"));
					discountDTO.setUpdatedAt(resultSet.getString("update_at"));
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}				
			}
			
		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(),e);
		} 
		
		return discountDTO;
	}

	public int addDiscount(DiscountDTO discountDTO) {
		
		int affected = 0;
		System.out.println(discountDTO);
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into discount (discount_name, discount_percentage, updated_at) values (?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setString(1, discountDTO.getDiscountName());
			statement.setDouble(2, discountDTO.getPercentage());
			statement.setString(3, DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(),e);
		} 

		return affected;
	}
	
	public int deleteDiscount(int id) {
		
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "delete from discount where discount_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(),e);
		} 
		return affected;
	}
	
	public int updateDiscount(DiscountDTO discountDTO) {
		
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "update discount set discount_name = ?, discount_percentage = ?, update_at = ? where discount_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, discountDTO.getDiscountName());
			statement.setDouble(2, discountDTO.getPercentage());
			statement.setString(3, DateTimeUtil.dateTimeFormat(LocalDateTime.now()));
			statement.setInt(4, discountDTO.getDiscountId());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(),e);
		} 
		
		return executeUpdate;
	}
}
