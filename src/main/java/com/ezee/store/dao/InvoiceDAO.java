package com.ezee.store.dao;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceDAO {
	private int invoiceId;
	private int customerId;
	private int employeeId;
	private int taxId;
	private int discountId;
	private Date invoiceDate;
	private String paymentMethod;
	private String paymentStatus;
}
