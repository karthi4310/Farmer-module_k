package com.farmer.farmermanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.farmer.farmermanagement.dto.BankDetailsDto;
import com.farmer.farmermanagement.dto.CropDto;
import com.farmer.farmermanagement.dto.FarmerDto;
import com.farmer.farmermanagement.dto.LandDetailsDto;
import com.farmer.farmermanagement.enums.BankName;
import com.farmer.farmermanagement.enums.CropType;
import com.farmer.farmermanagement.enums.Document;
import com.farmer.farmermanagement.enums.Education;
import com.farmer.farmermanagement.enums.Gender;
import com.farmer.farmermanagement.enums.IrrigationSource;
import com.farmer.farmermanagement.enums.PortalAccess;
import com.farmer.farmermanagement.enums.PortalRole;
import com.farmer.farmermanagement.enums.SoilTest;
import com.farmer.farmermanagement.service.FarmerService;

@ExtendWith(MockitoExtension.class)
class FarmerControllerManualMockTest {

	private MockMvc mockMvc;

	@Mock
	private FarmerService farmerService;

	@InjectMocks
	private FarmerController farmerController;

	private FarmerDto farmerDto;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(farmerController).build();

		LandDetailsDto landDetails = new LandDetailsDto(1L, "SURVEY123", 2.5, CropType.COTTON, SoilTest.YES,
				"certificate-path.pdf", "geotag123", 12.971598, 77.594566, IrrigationSource.BOREWELL, "High discharge",
				"Bangalore");

		BankDetailsDto bankDetails = new BankDetailsDto(BankName.SBI, "123956789812", "MG Road Branch", "SBIN0001234",
				"passbook-path.jpg");

		CropDto crop = CropDto.builder().photo("crop-photo.jpg").cropName("Wheat").cropType(CropType.COTTON)
				.areaInAcres(1.5).surveyNumber("SURVEY123").soilTest(SoilTest.YES).soilTestCertificate("soil-cert.pdf")
				.irrigationSource(IrrigationSource.BOREWELL).geoTag("geo123").latitude(12.971598).longitude(77.594566)
				.netIncome(20000).build();

		farmerDto = new FarmerDto(1L, "John", "M.", "Doe", "123456786012", "john.doe@example.com", "9876543210",
				Gender.MALE, Education.GRADUATE, Document.AADHARNUMBER, "document-path.pdf", LocalDate.of(1990, 1, 1),
				PortalAccess.ACTIVE, PortalRole.FARMER, "Organic", null, bankDetails, landDetails, List.of(crop));
	}

	@Test
	void testCreateFarmer() throws Exception {
		when(farmerService.createFarmer(any(FarmerDto.class))).thenReturn(farmerDto);

		mockMvc.perform(post("/api/farmers").contentType(MediaType.APPLICATION_JSON).content("""
				    {
				        "firstName":"John",
				        "middleName":"M.",
				        "lastName":"Doe",
				        "aadharNumber":"123456786012",
				        "email":"john.doe@example.com",
				        "phoneNumber":"9876543210",
				        "gender":"MALE",
				        "education":"GRADUATE",
				        "document":"AADHARNUMBER",
				        "documentPath":"document-path.pdf",
				        "dateOfBirth":"1990-01-01",
				        "portalAccess":"ACTIVE",
				        "portalRole":"FARMER",
				        "farmerType":"Organic",
				        "bankDetails": {
				            "bankName":"SBI",
				            "accountNumber":"123956789812",
				            "branchName":"MG Road Branch",
				            "ifscCode":"SBIN0001234",
				            "passbookAttachment":"passbook-path.jpg"
				        },
				        "landDetails": {
				            "surveyNumber":"SURVEY123",
				            "landSize":2.5,
				            "cropType":"COTTON",
				            "soilTest":"YES",
				            "soilTestCertificate":"certificate-path.pdf",
				            "geoTag":"geotag123",
				            "latitude":12.971598,
				            "longitude":77.594566,
				            "irrigationSource":"BOREWELL",
				            "borewellDischarge":"High discharge",
				            "borewellLocation":"Bangalore"
				        },
				        "crops":[
				            {
				                "photo":"crop-photo.jpg",
				                "cropName":"Wheat",
				                "cropType":"COTTON",
				                "areaInAcres":1.5,
				                "surveyNumber":"SURVEY123",
				                "soilTest":"YES",
				                "soilTestCertificate":"soil-cert.pdf",
				                "irrigationSource":"BOREWELL",
				                "geoTag":"geo123",
				                "latitude":12.971598,
				                "longitude":77.594566,
				                "netIncome":20000
				            }
				        ]
				    }
				""")).andExpect(status().isCreated()).andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.bankDetails.branchName").value("MG Road Branch"));
	}

	@Test
	void testGetFarmerById() throws Exception {
		when(farmerService.getFarmerById(1L)).thenReturn(farmerDto);

		mockMvc.perform(get("/api/farmers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value(farmerDto.getFirstName()))
				.andExpect(jsonPath("$.bankDetails.branchName").value(farmerDto.getBankDetails().getBranchName()));
	}
}
