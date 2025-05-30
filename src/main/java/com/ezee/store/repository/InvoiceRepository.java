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
import com.ezee.store.dao.InvoiceDAO;
import com.ezee.store.dto.InvoiceDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class InvoiceRepository {
	@Autowired
	private DatabaseConfiguration databaseConfiguration;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private TaxRepository taxRepository;

	@Autowired
	private DiscountRepository discountRepository;

	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");

	public List<InvoiceDTO> fetchAllInvoice() {
		List<InvoiceDTO> list = databaseConfiguration.getList();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			Statement statement = connection.createStatement();

			String query = "select invoice_id, customer_id, employee_id, tax_id, discount_id, invoice_date, payment_method, payment_status from invoice";

			@Cleanup
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				InvoiceDTO invoiceDTO = new InvoiceDTO();

				invoiceDTO.setInvoiceId(resultSet.getInt("invoice_id"));
				
				invoiceDTO.setCustomerDTO(customerRepository.getbyId(resultSet.getInt("customer_id")));
				invoiceDTO.setEmployeeDTO(employeeRepository.fetchById(resultSet.getInt("employee_id")));

				invoiceDTO.setTaxDTO(taxRepository.fetchTaxById(resultSet.getInt("tax_id")));
				invoiceDTO.setDiscountDTO(discountRepository.fetchDiscountById(resultSet.getInt("discount_id")));
				invoiceDTO.setInvoiceDate(resultSet.getDate("invoice_date"));
				invoiceDTO.setPaymentMethod(resultSet.getString("payment_method"));
				invoiceDTO.setPaymentStatus(resultSet.getString("payment_status"));

				list.add(invoiceDTO);
			}

			if (list.isEmpty()) {
				throw new ServiceException(ErrorCode.EMPTY_RESULT_DATA_EXCEPTION);
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return list;
	}

	public InvoiceDTO fetchInvoiceById(int id) {
		InvoiceDTO invoiceDTO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select invoice_id, customer_id, employee_id, tax_id, discount_id, invoice_date, payment_method, payment_status from invoice where invoice_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					invoiceDTO = new InvoiceDTO();

					invoiceDTO.setInvoiceId(resultSet.getInt("invoice_id"));
					invoiceDTO.setCustomerDTO(customerRepository.getbyId(resultSet.getInt("customer_id")));
					invoiceDTO.setEmployeeDTO(employeeRepository.fetchById(resultSet.getInt("employee_id")));
					invoiceDTO.setTaxDTO(taxRepository.fetchTaxById(resultSet.getInt("tax_id")));
					invoiceDTO.setDiscountDTO(discountRepository.fetchDiscountById(resultSet.getInt("discount_id")));
					invoiceDTO.setInvoiceDate(resultSet.getDate("invoice_date"));
					invoiceDTO.setPaymentMethod(resultSet.getString("payment_method"));
					invoiceDTO.setPaymentStatus(resultSet.getString("payment_status"));
				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return invoiceDTO;
	}

	public InvoiceDAO fetchInvoiceDAOById(int id) {
		InvoiceDAO invoiceDAO = null;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select invoice_id, customer_id, employee_id, tax_id, discount_id, invoice_date, payment_method, payment_status from invoice where invoice_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet != null) {
				if (resultSet.next()) {
					invoiceDAO = new InvoiceDAO();

					invoiceDAO.setInvoiceId(resultSet.getInt("invoice_id"));
					invoiceDAO.setCustomerId(resultSet.getInt("customer_id"));
					invoiceDAO.setEmployeeId(resultSet.getInt("employee_id"));
					invoiceDAO.setTaxId(resultSet.getInt("tax_id"));
					invoiceDAO.setDiscountId(resultSet.getInt("discount_id"));
					invoiceDAO.setInvoiceDate(resultSet.getDate("invoice_date"));
					invoiceDAO.setPaymentMethod(resultSet.getString("payment_method"));
					invoiceDAO.setPaymentStatus(resultSet.getString("payment_status"));

				} else {
					throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return invoiceDAO;
	}

	public int addInvoice(InvoiceDAO invoiceDAO) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "insert into invoice (customer_id, employee_id, tax_id, discount_id, invoice_date, payment_method, payment_status ) values (?, ?, ?, ?, ?, ?, ?)";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			int customerId = invoiceDAO.getCustomerId();
			customerRepository.getbyId(customerId);
			statement.setInt(1, customerId);

			int employeeId = invoiceDAO.getEmployeeId();
			employeeRepository.fetchById(employeeId);
			statement.setInt(2, employeeId);

			int taxId = invoiceDAO.getTaxId();
			taxRepository.fetchTaxById(taxId);
			statement.setInt(3, taxId);

			int discountId = invoiceDAO.getDiscountId();
			discountRepository.fetchDiscountById(discountId);
			statement.setInt(4, discountId);

			statement.setDate(5, invoiceDAO.getInvoiceDate());
			statement.setString(6, invoiceDAO.getPaymentMethod());
			statement.setString(7, invoiceDAO.getPaymentStatus());

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int deleteInvoice(int id) {
		int affected = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			
			String query = "delete from invoice where invoice_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			affected = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return affected;
	}

	public int updateInvoice(InvoiceDAO invoiceDAO) {
		int executeUpdate = 0;

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "update invoice set customer_id = ?, employee_id = ?, tax_id = ?, discount_id = ?, invoice_date = ?, payment_method = ?, payment_status = ? where invoice_id = ?";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);

			int customerId = invoiceDAO.getCustomerId();
			customerRepository.getbyId(customerId);
			statement.setInt(1, customerId);
			
			int employeeId = invoiceDAO.getEmployeeId();
			employeeRepository.fetchById(employeeId);
			statement.setInt(2, employeeId);

			int taxId = invoiceDAO.getTaxId();
			taxRepository.fetchTaxById(taxId);
			statement.setInt(3, taxId);

			int discountId = invoiceDAO.getDiscountId();
			discountRepository.fetchDiscountById(discountId);
			statement.setInt(4, discountId);

			statement.setDate(5, invoiceDAO.getInvoiceDate());
			statement.setString(6, invoiceDAO.getPaymentMethod());
			statement.setString(7, invoiceDAO.getPaymentStatus());
			statement.setInt(8, invoiceDAO.getInvoiceId());

			executeUpdate = statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}
}
