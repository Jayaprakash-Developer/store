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
import com.ezee.store.dao.EmployeeDAO;
import com.ezee.store.dto.EmployeeDetailDTO;
import com.ezee.store.dto.EmployeeDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class EmployeeRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepo;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<EmployeeDTO> fetchAllCustomer() {

		List<EmployeeDTO> list = databaseConfiguration.getList();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "SELECT employee_id, employee_detail_id, employee_role, employee_username, employee_password from employee";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				EmployeeDTO employeeDto = new EmployeeDTO();
				employeeDto.setEmployeeId(resultSet.getInt("employee_id"));
				employeeDto.setEmployeeRole(resultSet.getString("employee_role"));
				employeeDto.setUsername(resultSet.getString("employee_username"));
				employeeDto.setPassword(resultSet.getString("employee_password"));

				EmployeeDetailDTO fetchById = employeeDetailsRepo.fetchById(resultSet.getInt("employee_detail_id"));
				employeeDto.setEmployeeDetailId(fetchById);

				list.add(employeeDto);
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

	public int addemployee(EmployeeDAO employeeDao) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "INSERT INTO employee (employee_id, employee_detail_id, employee_role, employee_username, employee_password) VALUES ( ?, ?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setInt(1, employeeDao.getEmployeeId());
			statement.setInt(2, employeeDao.getEmployeeDetailId());
			statement.setString(3, employeeDao.getEmployeeRole());
			statement.setString(4, employeeDao.getUsername());
			statement.setString(5, employeeDao.getPassword());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}

	public EmployeeDTO fetchById(int id) {

		EmployeeDTO employeeDto = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT employee_id, employee_detail_id, employee_role, employee_username, employee_password from employee where employee_id=?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					employeeDto = new EmployeeDTO();
					employeeDto.setEmployeeId(resultSet.getInt("employee_id"));
					employeeDto.setEmployeeRole(resultSet.getString("employee_role"));
					employeeDto.setUsername(resultSet.getString("employee_username"));
					employeeDto.setPassword(resultSet.getString("employee_password"));

					EmployeeDetailDTO fetchById = employeeDetailsRepo.fetchById(resultSet.getInt("employee_detail_id"));
					employeeDto.setEmployeeDetailId(fetchById);

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return employeeDto;
	}

	public int delete(int id) {

		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "DELETE FROM employee WHERE employee_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(EmployeeDAO employeeDao) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "UPDATE employee SET employee_id=?, employee_detail_id=?, employee_role=?, employee_username=?, employee_password=? WHERE employee_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, employeeDao.getEmployeeId());
			int employeeDetailId = employeeDao.getEmployeeDetailId();
			employeeDetailsRepo.fetchById(employeeDetailId);
			statement.setInt(2, employeeDetailId);
			statement.setString(3, employeeDao.getEmployeeRole());
			statement.setString(4, employeeDao.getUsername());
			statement.setString(5, employeeDao.getPassword());

			affected = statement.executeUpdate();
			System.out.println("update executed.");

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public EmployeeDAO fetchDaoById(int id) {
		EmployeeDAO employeeDao = null;

		try {
			Connection connection = dataSource.getConnection();

			String query = "SELECT employee_id, employee_detail_id, employee_role, employee_username, employee_password from employee where employee_id=?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					employeeDao = new EmployeeDAO();
					employeeDao.setEmployeeId(resultSet.getInt("employee_id"));
					employeeDao.setEmployeeDetailId(resultSet.getInt(id));
					employeeDao.setEmployeeRole(resultSet.getString("employee_role"));
					employeeDao.setUsername(resultSet.getString("employee_username"));
					employeeDao.setPassword(resultSet.getString("employee_password"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return employeeDao;
	}
}
