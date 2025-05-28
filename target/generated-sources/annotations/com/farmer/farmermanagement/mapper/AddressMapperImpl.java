package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.AddressDto;
import com.farmer.farmermanagement.entity.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-29T01:00:34+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.36.0.v20231114-0937, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDto toDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto.AddressDtoBuilder addressDto = AddressDto.builder();

        addressDto.city( address.getCity() );
        addressDto.district( address.getDistrict() );
        addressDto.pincode( address.getPincode() );
        addressDto.state( address.getState() );
        addressDto.street( address.getStreet() );

        return addressDto.build();
    }

    @Override
    public Address toEntity(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setCity( addressDto.getCity() );
        address.setDistrict( addressDto.getDistrict() );
        address.setPincode( addressDto.getPincode() );
        address.setState( addressDto.getState() );
        address.setStreet( addressDto.getStreet() );

        return address;
    }
}
