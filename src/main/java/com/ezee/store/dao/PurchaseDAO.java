package com.ezee.store.dao;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDAO {
	private int purchaseId;
	private int venderId;
	private int taxId;
	private String paymentStatus;
	private Date purchaseDate;
}
