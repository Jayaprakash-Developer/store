package com.ezee.store.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductDTO {
	private int returnProductId;
	@JsonProperty(value = "invoice")
	private InvoiceDTO invoiceDTO;
	private double refundAmount;
	private String refundMethod;
	private Date refundDate;
}
