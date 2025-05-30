package com.ezee.store.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class CategoryDTO {
	private int categoryId;
	private String categoryName;
	private String categoryDescription;
	private Date updatedAt;
}
