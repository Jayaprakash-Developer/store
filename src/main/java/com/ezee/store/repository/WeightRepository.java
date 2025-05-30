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
import com.ezee.store.dto.WeightDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class WeightRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<WeightDTO> fetchAllWeight() {

		List<WeightDTO> list = databaseConfiguration.getList();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "SELECT weight_id, weight_value, weight_unit, updated_at from weight";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				WeightDTO weightDto = new WeightDTO();
				weightDto.setWeightId(resultSet.getInt("weight_id"));
				weightDto.setWeightValue(resultSet.getDouble("weight_value"));
				weightDto.setWeightUnit(resultSet.getString("weight_unit"));
				weightDto.setUpdatedAt(resultSet.getDate("updated_at"));
				list.add(weightDto);
			}
			System.out.println("executed weight repo. fetchall");
			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return list;
	}

	public WeightDTO fetchById(int id) {

		WeightDTO weightDto = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT weight_id, weight_value, weight_unit, updated_at from weight where  weight_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					weightDto = new WeightDTO();
					weightDto.setWeightId(resultSet.getInt("weight_id"));
					weightDto.setWeightValue(resultSet.getDouble("weight_value"));
					weightDto.setWeightUnit(resultSet.getString("weight_unit"));
					weightDto.setUpdatedAt(resultSet.getDate("updated_at"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");
		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return weightDto;
	}

	public int delete(int id) {

		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "DELETE FROM weight WHERE weight_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(WeightDTO weightDto) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "UPDATE weight SET weight_value=?, weight_unit=?, updated_at=?, updated_at=?  where weight_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, weightDto.getWeightId());
			statement.setDouble(2, weightDto.getWeightValue());
			statement.setString(3, weightDto.getWeightUnit());
			statement.setDate(4, weightDto.getUpdatedAt());
			statement.executeUpdate();
			System.out.println("update executed.");

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;

	}

	public int addWeight(WeightDTO weightDto) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "INSERT INTO weight (weight_id, weight_value, weight_unit, updated_at) VALUES ( ?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, weightDto.getWeightId());
			statement.setDouble(2, weightDto.getWeightValue());
			statement.setString(3, weightDto.getWeightUnit());
			statement.setDate(4, weightDto.getUpdatedAt());
			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}
}
