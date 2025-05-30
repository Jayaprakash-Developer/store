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
import com.ezee.store.dto.CustomerDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class CustomerRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<CustomerDTO> fetchAllCustomer() {
		List<CustomerDTO> list = databaseConfiguration.getList();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "SELECT customer_id, customer_name, customer_phone, customer_email, customer_address, customer_registration_date, updated_at from customer";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				CustomerDTO customerDTO = new CustomerDTO();

				customerDTO.setCustomerId(resultSet.getInt("customer_id"));
				customerDTO.setCustomerName(resultSet.getString("customer_name"));
				customerDTO.setEmail(resultSet.getString("customer_email"));
				customerDTO.setPhoneNo(resultSet.getLong("customer_phone"));
				customerDTO.setAddress(resultSet.getString("customer_address"));
				customerDTO.setRegisterDate(resultSet.getDate("customer_registration_date"));
				customerDTO.setUpdated_at(resultSet.getDate("updated_at"));

				list.add(customerDTO);
			}

			System.out.println("executed customer repo. fetchall");

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return list;
	}

	public boolean existsById(int id) {

		boolean exists = false;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT COUNT(customer_id) FROM customer WHERE customer_id = ?";

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

	public int addCustomer(CustomerDTO customerDTO) {

		int executeUpdate = 0;

		try {
			if (existsById(customerDTO.getCustomerId())) {
				throw new ServiceException(ErrorCode.ID_ALREADY_EXISTS);
			}
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "INSERT INTO customer (customer_id, customer_name, customer_phone, customer_email, customer_address, customer_registration_date, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, customerDTO.getCustomerId());
			statement.setString(2, customerDTO.getCustomerName());
			statement.setLong(3, customerDTO.getPhoneNo());
			statement.setString(4, customerDTO.getEmail());
			statement.setString(5, customerDTO.getAddress());
			statement.setDate(6, customerDTO.getRegisterDate());
			statement.setDate(7, customerDTO.getUpdated_at());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.DATABASE_ERROR, e);
		}

		return executeUpdate;
	}

	public CustomerDTO getbyId(int id) {

		CustomerDTO customerDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT customer_id, customer_name, customer_phone, customer_email, customer_address, customer_registration_date, updated_at from customer WHERE customer_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					customerDTO = new CustomerDTO();
					customerDTO.setCustomerId(resultSet.getInt("customer_id"));
					customerDTO.setCustomerName(resultSet.getString("customer_name"));
					customerDTO.setEmail(resultSet.getString("customer_email"));
					customerDTO.setPhoneNo(resultSet.getLong("customer_phone"));
					customerDTO.setAddress(resultSet.getString("customer_address"));
					customerDTO.setRegisterDate(resultSet.getDate("customer_registration_date"));
					customerDTO.setUpdated_at(resultSet.getDate("updated_at"));
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return customerDTO;
	}

	public int delete(int id) {

		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "DELETE FROM customer WHERE customer_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(CustomerDTO customerDTO) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "UPDATE customer SET customer_name = ?, customer_phone = ?, customer_email = ?, customer_address = ?, customer_registration_date=?, updated_at=?  WHERE customer_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, customerDTO.getCustomerName());
			statement.setLong(2, customerDTO.getPhoneNo());
			statement.setString(3, customerDTO.getEmail());
			statement.setString(4, customerDTO.getAddress());
			statement.setDate(5, customerDTO.getRegisterDate());
			statement.setDate(6, customerDTO.getUpdated_at());
			statement.setInt(7, customerDTO.getCustomerId());

			executeUpdate = statement.executeUpdate();
			System.out.println(executeUpdate);

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;

	}
}
