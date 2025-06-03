package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.AddressDto;
import com.farmer.farmermanagement.entity.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T10:27:59+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDto toDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto.AddressDtoBuilder addressDto = AddressDto.builder();

        addressDto.street( address.getStreet() );
        addressDto.city( address.getCity() );
        addressDto.district( address.getDistrict() );
        addressDto.state( address.getState() );
        addressDto.pincode( address.getPincode() );

        return addressDto.build();
    }

    @Override
    public Address toEntity(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setStreet( addressDto.getStreet() );
        address.setCity( addressDto.getCity() );
        address.setDistrict( addressDto.getDistrict() );
        address.setState( addressDto.getState() );
        address.setPincode( addressDto.getPincode() );

        return address;
    }
}
