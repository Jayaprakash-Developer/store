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
import com.ezee.store.dto.TaxDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.util.DateTimeUtil;

import lombok.Cleanup;

@Repository
public class TaxRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Autowired
	private DataSource dataSource;
	
	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");
	
	public List<TaxDTO> fetchAllTax() {
		List<TaxDTO> list = databaseConfiguration.getList();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();
			
			String query = "select tax_id, tax_name, tax_percentage, updated_at from tax";
			
			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				TaxDTO taxDTO = new TaxDTO();
				taxDTO.setPercentage(resultSet.getDouble("tax_percentage"));
				taxDTO.setTaxId(resultSet.getInt("tax_id"));
				taxDTO.setTaxName(resultSet.getString("tax_name"));
				taxDTO.setUpdatedAt(resultSet.getString("updated_at"));
				
				list.add(taxDTO);
			}
			
			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}
		
		return list;
	}

	public TaxDTO fetchTaxById(int id) {
		
		TaxDTO taxDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select tax_id, tax_name, tax_percentage, updated_at from tax where tax_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					taxDTO = new TaxDTO();
					
					taxDTO.setPercentage(resultSet.getDouble("tax_percentage"));
					taxDTO.setTaxId(resultSet.getInt("tax_id"));
					taxDTO.setTaxName(resultSet.getString("tax_name"));
					taxDTO.setUpdatedAt(resultSet.getString("updated_at"));
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}				
			}
			
		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}
		
		return taxDTO;
	}

	public int addTax(TaxDTO taxDTO) {
		
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "insert into tax (tax_name, tax_percentage, updated_at) values (?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, taxDTO.getTaxName());
			statement.setDouble(2, taxDTO.getPercentage());
			statement.setString(3, DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		} 

		return affected;
	}
	
	public int deleteTax(int id) {
		
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "delete from tax where tax_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return affected;
	}
	
	public int updateTax(TaxDTO taxDTO) {
		
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "update tax set tax_name = ?, tax_percentage = ?, updated_at = ? where tax_id = ?";
			
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setString(1, taxDTO.getTaxName());
			statement.setDouble(2, taxDTO.getPercentage());
			statement.setString(3, DateTimeUtil.dateTimeFormat(LocalDateTime.now()));
			statement.setInt(4, taxDTO.getTaxId());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return executeUpdate;
		
	}
}
