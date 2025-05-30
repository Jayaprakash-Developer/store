package com.ezee.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailDTO {
	private int returnDetailId;
	
	@JsonProperty(value = "returnProduct")
	private ReturnProductDTO returnProductDTO;
	
	@JsonProperty(value = "product")
	private ProductDTO productDTO;
	
	private int quantity;
	private double price;
}
