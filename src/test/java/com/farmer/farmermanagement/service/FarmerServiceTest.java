package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.FarmerDto;
import com.farmer.farmermanagement.entity.Farmer;
import com.farmer.farmermanagement.exception.FarmerNotFoundException;
import com.farmer.farmermanagement.mapper.AddressMapper;
import com.farmer.farmermanagement.mapper.CropMapper;
import com.farmer.farmermanagement.mapper.FarmerMapper;
import com.farmer.farmermanagement.repository.FarmerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FarmerServiceTest {

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private FarmerMapper farmerMapper;

    @Mock
    private CropMapper cropMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private FarmerService farmerService;

    private Farmer farmerEntity;
    private FarmerDto farmerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        farmerEntity = new Farmer();
        farmerEntity.setId(1L);
        farmerEntity.setFirstName("John");

        farmerDto = new FarmerDto();
        farmerDto.setId(1L);
        farmerDto.setFirstName("John");
    }

    @Test
    void testCreateFarmer() {
        when(farmerMapper.toEntity(any(FarmerDto.class))).thenReturn(farmerEntity);
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmerEntity);
        when(farmerMapper.toDto(any(Farmer.class))).thenReturn(farmerDto);

        FarmerDto result = farmerService.createFarmer(farmerDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testUpdateFarmer_whenExists() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmerEntity));
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmerEntity);
        when(farmerMapper.toDto(any(Farmer.class))).thenReturn(farmerDto);

        FarmerDto result = farmerService.updateFarmer(1L, farmerDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testUpdateFarmer_whenNotFound() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FarmerNotFoundException.class, () -> farmerService.updateFarmer(1L, farmerDto));
    }

    @Test
    void testGetAllFarmers() {
        when(farmerRepository.findAll()).thenReturn(Arrays.asList(farmerEntity));
        when(farmerMapper.toDto(any(Farmer.class))).thenReturn(farmerDto);

        List<FarmerDto> result = farmerService.getAllFarmers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testGetFarmerById_whenExists() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmerEntity));
        when(farmerMapper.toDto(farmerEntity)).thenReturn(farmerDto);

        FarmerDto result = farmerService.getFarmerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetFarmerById_whenNotFound() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FarmerNotFoundException.class, () -> farmerService.getFarmerById(1L));
    }

    @Test
    void testDeleteFarmer_whenExists() {
        when(farmerRepository.existsById(1L)).thenReturn(true);

        farmerService.deleteFarmer(1L);

        verify(farmerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFarmer_whenNotFound() {
        when(farmerRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> farmerService.deleteFarmer(1L));
    }
}
