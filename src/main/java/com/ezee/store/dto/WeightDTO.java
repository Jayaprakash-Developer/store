package com.ezee.store.dto;

import java.sql.Date;

import lombok.Data;
@Data
public class WeightDTO {
	private int weightId;
	private double weightValue;
	private String weightUnit;
	private Date updatedAt;
}
