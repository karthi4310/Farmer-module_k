package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.CropDto;
import com.farmer.farmermanagement.dto.FarmerDto;
import com.farmer.farmermanagement.entity.Crop;
import com.farmer.farmermanagement.entity.Farmer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T16:48:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class FarmerMapperImpl implements FarmerMapper {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private BankDetailsMapper bankDetailsMapper;
    @Autowired
    private LandDetailsMapper landDetailsMapper;
    @Autowired
    private CropMapper cropMapper;

    @Override
    public Farmer toEntity(FarmerDto dto) {
        if ( dto == null ) {
            return null;
        }

        Farmer farmer = new Farmer();

        farmer.setAddress( addressMapper.toEntity( dto.getAddress() ) );
        farmer.setBankDetails( bankDetailsMapper.toEntity( dto.getBankDetails() ) );
        farmer.setLandDetails( landDetailsMapper.toEntity( dto.getLandDetails() ) );
        farmer.setCrops( cropDtoListToCropList( dto.getCrops() ) );
        farmer.setId( dto.getId() );
        farmer.setFirstName( dto.getFirstName() );
        farmer.setMiddleName( dto.getMiddleName() );
        farmer.setLastName( dto.getLastName() );
        farmer.setAadharNumber( dto.getAadharNumber() );
        farmer.setEmail( dto.getEmail() );
        farmer.setPhoneNumber( dto.getPhoneNumber() );
        farmer.setGender( dto.getGender() );
        farmer.setEducation( dto.getEducation() );
        farmer.setDocument( dto.getDocument() );
        farmer.setDocumentPath( dto.getDocumentPath() );
        farmer.setDateOfBirth( dto.getDateOfBirth() );
        farmer.setPortalAccess( dto.getPortalAccess() );
        farmer.setPortalRole( dto.getPortalRole() );
        farmer.setFarmerType( dto.getFarmerType() );

        return farmer;
    }

    @Override
    public FarmerDto toDto(Farmer entity) {
        if ( entity == null ) {
            return null;
        }

        FarmerDto farmerDto = new FarmerDto();

        farmerDto.setAddress( addressMapper.toDto( entity.getAddress() ) );
        farmerDto.setBankDetails( bankDetailsMapper.toDto( entity.getBankDetails() ) );
        farmerDto.setLandDetails( landDetailsMapper.toDto( entity.getLandDetails() ) );
        farmerDto.setCrops( cropListToCropDtoList( entity.getCrops() ) );
        farmerDto.setId( entity.getId() );
        farmerDto.setFirstName( entity.getFirstName() );
        farmerDto.setMiddleName( entity.getMiddleName() );
        farmerDto.setLastName( entity.getLastName() );
        farmerDto.setAadharNumber( entity.getAadharNumber() );
        farmerDto.setEmail( entity.getEmail() );
        farmerDto.setPhoneNumber( entity.getPhoneNumber() );
        farmerDto.setGender( entity.getGender() );
        farmerDto.setEducation( entity.getEducation() );
        farmerDto.setDocument( entity.getDocument() );
        farmerDto.setDocumentPath( entity.getDocumentPath() );
        farmerDto.setDateOfBirth( entity.getDateOfBirth() );
        farmerDto.setPortalAccess( entity.getPortalAccess() );
        farmerDto.setPortalRole( entity.getPortalRole() );
        farmerDto.setFarmerType( entity.getFarmerType() );

        return farmerDto;
    }

    protected List<Crop> cropDtoListToCropList(List<CropDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Crop> list1 = new ArrayList<Crop>( list.size() );
        for ( CropDto cropDto : list ) {
            list1.add( cropMapper.toEntity( cropDto ) );
        }

        return list1;
    }

    protected List<CropDto> cropListToCropDtoList(List<Crop> list) {
        if ( list == null ) {
            return null;
        }

        List<CropDto> list1 = new ArrayList<CropDto>( list.size() );
        for ( Crop crop : list ) {
            list1.add( cropMapper.toDto( crop ) );
        }

        return list1;
    }
}
