package com.ezee.store.dao;

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
public class ReturnDetailDAO {
	private int returnDetailId;
	private int returnProductId;
	private int productId;
	private int quantity;
	private double price;
}
