package com.ezee.store.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class VendorDTO {
	private int vendorId;
	private String vendorName;
	private long vendorPhone;
	private String vendorEmail;
	private String vendoraddress;
	private Date vendorRegistrationData;
	private String vendorStatus;
	private Date updatedAt;
}
