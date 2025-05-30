package com.ezee.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class InvoiceDetailDTO {
	private int invoiceDetailId; 
	
	@JsonProperty("invoice")
	private InvoiceDTO invoiceDTO;
	
	@JsonProperty("product")
	private ProductDTO productDTO;
	private int quantity;
	private double price;
}
