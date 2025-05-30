package com.ezee.store.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
	private int employeeId;
	private EmployeeDetailDTO employeeDetailId;
	private String employeeRole;
	private String username;
	private String password;
}
