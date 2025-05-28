package com.farmer.farmermanagement.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	private String street;
	private String city;
	private String district;
	private String state;
	private String pincode;
}
