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
import com.ezee.store.dto.EmployeeDetailDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class EmployeeDetailsRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public boolean existsById(int id) {

		boolean exists = false;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT COUNT(employee_detail_id) FROM employee_detail WHERE employee_detail_id = ?";

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

	public List<EmployeeDetailDTO> fetchAllEmployee() {

		List<EmployeeDetailDTO> list = databaseConfiguration.getList();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "SELECT employee_detail_id, employee_name, gender, date_of_birth, email, phone_number, hire_date, salary, address, status, updated_at from employee_detail";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				EmployeeDetailDTO employeeDetailDto = new EmployeeDetailDTO();
				employeeDetailDto.setEmployeeDetailId(resultSet.getInt("employee_detail_id"));
				employeeDetailDto.setEmployeeName(resultSet.getString("employee_name"));
				employeeDetailDto.setGender(resultSet.getString("gender"));
				employeeDetailDto.setDateOfBirth(resultSet.getDate("date_of_birth"));
				employeeDetailDto.setEmail(resultSet.getString("email"));
				employeeDetailDto.setPhoneNo(resultSet.getString("phone_number"));
				employeeDetailDto.setHireDate(resultSet.getDate("hire_date"));
				employeeDetailDto.setSalary(resultSet.getDouble("salary"));
				employeeDetailDto.setAddress(resultSet.getString("address"));
				employeeDetailDto.setStatus(resultSet.getString("status"));
				employeeDetailDto.setUpdated_at(resultSet.getDate("updated_at"));
				list.add(employeeDetailDto);
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

	public EmployeeDetailDTO fetchById(int id) {

		EmployeeDetailDTO employeeDetailDto = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT employee_detail_id, employee_name, gender, date_of_birth, email, phone_number, hire_date, salary, address, status, updated_at from employee_detail";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					employeeDetailDto = new EmployeeDetailDTO();
					employeeDetailDto.setEmployeeDetailId(resultSet.getInt("employee_detail_id"));
					employeeDetailDto.setEmployeeName(resultSet.getString("employee_name"));
					employeeDetailDto.setGender(resultSet.getString("gender"));
					employeeDetailDto.setDateOfBirth(resultSet.getDate("date_of_birth"));
					employeeDetailDto.setEmail(resultSet.getString("email"));
					employeeDetailDto.setPhoneNo(resultSet.getString("phone_number"));
					employeeDetailDto.setHireDate(resultSet.getDate("hire_date"));
					employeeDetailDto.setSalary(resultSet.getDouble("salary"));
					employeeDetailDto.setAddress(resultSet.getString("address"));
					employeeDetailDto.setStatus(resultSet.getString("status"));
					employeeDetailDto.setUpdated_at(resultSet.getDate("updated_at"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return employeeDetailDto;
	}

	public int delete(int id) {

		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "DELETE FROM employee_detail WHERE employee_detail_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(EmployeeDetailDTO employeeDetailDto) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "UPDATE employee_detail SET employee_detail_id=?, employee_name=?, gender=?, date_of_birth=?, email=?, phone_number=?, hire_date=?, salary=?, address=?, status=?, updated_at=?  where employee_detail_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, employeeDetailDto.getEmployeeDetailId());
			statement.setString(2, employeeDetailDto.getEmployeeName());
			statement.setString(3, employeeDetailDto.getGender());
			statement.setDate(4, employeeDetailDto.getDateOfBirth());
			statement.setString(5, employeeDetailDto.getEmail());
			statement.setString(6, employeeDetailDto.getPhoneNo());
			statement.setDate(7, employeeDetailDto.getHireDate());
			statement.setDouble(8, employeeDetailDto.getSalary());
			statement.setString(9, employeeDetailDto.getAddress());
			statement.setString(10, employeeDetailDto.getStatus());
			statement.setDate(11, employeeDetailDto.getUpdated_at());
			executeUpdate = statement.executeUpdate();
			System.out.println("update executed.");

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;

	}

	public int addProduct(EmployeeDetailDTO employeeDetailDto) {

		int executeUpdate = 0;

		try {
			if (existsById(employeeDetailDto.getEmployeeDetailId())) {
				throw new ServiceException(ErrorCode.ID_ALREADY_EXISTS);
			}
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "INSERT INTO employee_detail (employee_detail_id, employee_name, gender, date_of_birth, email, phone_number, hire_date, salary, address, status, updated_at ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, employeeDetailDto.getEmployeeDetailId());
			statement.setString(2, employeeDetailDto.getEmployeeName());
			statement.setString(3, employeeDetailDto.getGender());
			statement.setDate(4, employeeDetailDto.getDateOfBirth());
			statement.setString(5, employeeDetailDto.getEmail());
			statement.setString(6, employeeDetailDto.getPhoneNo());
			statement.setDate(7, employeeDetailDto.getHireDate());
			statement.setDouble(8, employeeDetailDto.getSalary());
			statement.setString(9, employeeDetailDto.getAddress());
			statement.setString(10, employeeDetailDto.getStatus());
			statement.setDate(11, employeeDetailDto.getUpdated_at());
			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}
}
