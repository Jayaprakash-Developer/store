package com.ezee.store.dto;

import java.sql.Date;

import lombok.Data;
@Data
public class EmployeeDetailDTO {
	private int employeeDetailId;
	private String employeeName;
	private String gender;
	private Date dateOfBirth;
	private String email;
	private String phoneNo;
	private Date hireDate;
	private double salary;
	private String address;
	private String status;
	private Date updated_at;
	

}
