package com.ezee.store.dto;

import java.sql.Date;

import lombok.Data;
@Data
public class CustomerDTO {
	private int customerId;
	private String customerName;
	private String email;
	private long phoneNo;
	private String address;
	private Date registerDate;
	private Date updated_at;
}
