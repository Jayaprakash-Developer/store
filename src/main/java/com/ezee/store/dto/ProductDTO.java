package com.ezee.store.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ProductDTO {
	private int productId;
	private String productName;
	private CategoryDTO categoryId;
	private WeightDTO weightId;
	private String packageType;
	private String brandName;
	private double price;
	private Date expireDay;
	private String description;
	private Date updatedAt;

}
