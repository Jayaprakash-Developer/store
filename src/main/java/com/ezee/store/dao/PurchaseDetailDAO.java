package com.ezee.store.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseDetailDAO {
	private int purchaseDetailId;
	private int purchaseId;
	private int productId;	
	private int quantity;
	private double price;
}
