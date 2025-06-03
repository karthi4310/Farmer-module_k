package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.BankDetailsDto;
import com.farmer.farmermanagement.entity.BankDetails;
import com.farmer.farmermanagement.enums.BankName;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T10:27:59+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class BankDetailsMapperImpl implements BankDetailsMapper {

    @Override
    public BankDetails toEntity(BankDetailsDto dto) {
        if ( dto == null ) {
            return null;
        }

        BankDetails bankDetails = new BankDetails();

        bankDetails.setAccountNumber( dto.getAccountNumber() );
        bankDetails.setIfscCode( dto.getIfscCode() );
        if ( dto.getBankName() != null ) {
            bankDetails.setBankName( dto.getBankName().name() );
        }

        return bankDetails;
    }

    @Override
    public BankDetailsDto toDto(BankDetails entity) {
        if ( entity == null ) {
            return null;
        }

        BankDetailsDto.BankDetailsDtoBuilder bankDetailsDto = BankDetailsDto.builder();

        if ( entity.getBankName() != null ) {
            bankDetailsDto.bankName( Enum.valueOf( BankName.class, entity.getBankName() ) );
        }
        bankDetailsDto.accountNumber( entity.getAccountNumber() );
        bankDetailsDto.ifscCode( entity.getIfscCode() );

        return bankDetailsDto.build();
    }
}
