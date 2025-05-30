package com.ezee.store.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
	private int invoiceId;
	
	@JsonProperty(value = "customer")
	private CustomerDTO customerDTO;
	
	@JsonProperty(value = "employee")
	private EmployeeDTO employeeDTO;
	
	@JsonProperty(value = "tax")
	private TaxDTO taxDTO;
	
	@JsonProperty(value = "discount")
	private DiscountDTO discountDTO;
	private Date invoiceDate;
	private String paymentMethod;
	private String paymentStatus;
}
