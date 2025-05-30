package com.ezee.store.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
	private int purchaseId;
	
	@JsonProperty(value = "vendor")
	private VendorDTO vendorDTO;

	@JsonProperty(value = "tax")
	private TaxDTO taxDTO;
	private String paymentStatus;
	private Date purchaseDate;
}
