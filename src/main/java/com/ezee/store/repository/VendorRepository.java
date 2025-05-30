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
import com.ezee.store.dto.VendorDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class VendorRepository {
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

			String query = "SELECT COUNT(vendor_id) FROM vendor WHERE vendor_id = ?";

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

	public List<VendorDTO> fetchAllVendor() {

		List<VendorDTO> list = databaseConfiguration.getList();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "SELECT vendor_id, vendor_name, vendor_phone, vendor_email, vendor_address, vendor_registration_date, vendor_status, updated_at from vendor";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				VendorDTO vendorDto = new VendorDTO();
				vendorDto.setVendorId(resultSet.getInt("vendor_id"));
				vendorDto.setVendorName(resultSet.getString("vendor_name"));
				vendorDto.setVendorPhone(resultSet.getLong("vendor_phone"));
				vendorDto.setVendorEmail(resultSet.getString("vendor_email"));
				vendorDto.setVendoraddress(resultSet.getString("vendor_address"));
				vendorDto.setVendorRegistrationData(resultSet.getDate("vendor_registration_date"));
				vendorDto.setVendorStatus(resultSet.getString("vendor_status"));
				vendorDto.setUpdatedAt(resultSet.getDate("updated_at"));
				list.add(vendorDto);
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

	public int addvendor(VendorDTO vendorDto) {

		int executeUpdate = 0;

		try {
			if (existsById(vendorDto.getVendorId())) {
				throw new ServiceException(ErrorCode.ID_ALREADY_EXISTS);
			}
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "INSERT INTO vendor (vendor_id, vendor_name, vendor_phone, vendor_email, vendor_address, vendor_registration_date, vendor_status, updated_at) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, vendorDto.getVendorId());
			statement.setString(2, vendorDto.getVendorName());
			statement.setLong(3, vendorDto.getVendorPhone());
			statement.setString(4, vendorDto.getVendorEmail());
			statement.setString(5, vendorDto.getVendoraddress());
			statement.setDate(6, vendorDto.getVendorRegistrationData());
			statement.setString(7, vendorDto.getVendorStatus());
			statement.setDate(8, vendorDto.getUpdatedAt());
			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}

	public VendorDTO fetchById(int id) {

		VendorDTO vendorDto = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT vendor_id, vendor_name, vendor_phone, vendor_email, vendor_address, vendor_registration_date, vendor_status, updated_at from vendor where vendor_id= ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null) {
				if (resultSet.next()) {
					vendorDto = new VendorDTO();
					vendorDto.setVendorId(resultSet.getInt("vendor_id"));
					vendorDto.setVendorName(resultSet.getString("vendor_name"));
					vendorDto.setVendorPhone(resultSet.getLong("vendor_phone"));
					vendorDto.setVendorEmail(resultSet.getString("vendor_email"));
					vendorDto.setVendoraddress(resultSet.getString("vendor_address"));
					vendorDto.setVendorRegistrationData(resultSet.getDate("vendor_registration_date"));
					vendorDto.setVendorStatus(resultSet.getString("vendor_status"));
					vendorDto.setUpdatedAt(resultSet.getDate("updated_at"));
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

			System.out.println("fetchbyId executed.");
		} catch (SQLException e) {
			LOGGER.error("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return vendorDto;
	}

	public int delete(int id) {

		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "DELETE FROM vendor WHERE vendor_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int update(VendorDTO vendorDTO) {

		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "UPDATE vendor SET vendor_name = ?,  vendor_phone = ?, vendor_email = ?, vendor_address = ?, vendor_registration_date= ? , vendor_status= ? , updated_at= ? WHERE vendor_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, vendorDTO.getVendorName());
			statement.setLong(2, vendorDTO.getVendorPhone());
			statement.setString(3, vendorDTO.getVendorEmail());
			statement.setString(4, vendorDTO.getVendoraddress());
			statement.setDate(5, vendorDTO.getVendorRegistrationData());
			statement.setString(6, vendorDTO.getVendorStatus());
			statement.setDate(7, vendorDTO.getUpdatedAt());
			statement.setInt(8, vendorDTO.getVendorId());
			executeUpdate = statement.executeUpdate();
			System.out.println("update executed.");

		} catch (SQLException e) {
			LOGGER.info("Error occuring on message: {}/n, data: {}", e.getMessage(), e);
		}

		return executeUpdate;

	}
}
