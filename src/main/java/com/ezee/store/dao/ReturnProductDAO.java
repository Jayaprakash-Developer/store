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
public class ReturnProductDAO {
	private int returnProductId;
	private int invoiceId;
	private double refundAmount;
	private String refundMethod;
	private Date refundDate;
}
