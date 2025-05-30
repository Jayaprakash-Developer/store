package com.ezee.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaxDTO {
	private int taxId;
	private String taxName;
	private double percentage;
	private String updatedAt;
}
