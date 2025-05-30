package com.ezee.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetailDTO {
	private int purchaseDetailId;
	
	@JsonProperty(value = "purchase")
	private PurchaseDTO purchaseDTO;
	
	@JsonProperty(value = "product")
	private ProductDTO productDTO;	
	private int quantity;
	private double price;
}
