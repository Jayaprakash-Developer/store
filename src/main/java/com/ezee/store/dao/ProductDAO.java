package com.ezee.store.dao;

import java.sql.Date;

import lombok.Data;
@Data
public class ProductDAO {
	private int productId;
	private String productName;
	private int categoryId;
	private int weightId;
	private String packageType;
	private String brandName;
	private double price;
	private Date expireDay;
	private String description;
	private Date updatedAt;

}
