package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.LandDetailsDto;
import com.farmer.farmermanagement.entity.LandDetails;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T10:27:59+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class LandDetailsMapperImpl implements LandDetailsMapper {

    @Override
    public LandDetailsDto toDto(LandDetails landDetails) {
        if ( landDetails == null ) {
            return null;
        }

        LandDetailsDto landDetailsDto = new LandDetailsDto();

        landDetailsDto.setId( landDetails.getId() );
        landDetailsDto.setSurveyNumber( landDetails.getSurveyNumber() );
        landDetailsDto.setLandSize( landDetails.getLandSize() );
        landDetailsDto.setCropType( landDetails.getCropType() );
        landDetailsDto.setSoilTest( landDetails.getSoilTest() );
        landDetailsDto.setSoilTestCertificate( landDetails.getSoilTestCertificate() );
        landDetailsDto.setGeoTag( landDetails.getGeoTag() );
        landDetailsDto.setLatitude( landDetails.getLatitude() );
        landDetailsDto.setLongitude( landDetails.getLongitude() );
        landDetailsDto.setIrrigationSource( landDetails.getIrrigationSource() );
        landDetailsDto.setBorewellDischarge( landDetails.getBorewellDischarge() );
        landDetailsDto.setBorewellLocation( landDetails.getBorewellLocation() );

        return landDetailsDto;
    }

    @Override
    public LandDetails toEntity(LandDetailsDto landDetailsDto) {
        if ( landDetailsDto == null ) {
            return null;
        }

        LandDetails landDetails = new LandDetails();

        landDetails.setId( landDetailsDto.getId() );
        landDetails.setSurveyNumber( landDetailsDto.getSurveyNumber() );
        landDetails.setLandSize( landDetailsDto.getLandSize() );
        landDetails.setCropType( landDetailsDto.getCropType() );
        landDetails.setSoilTest( landDetailsDto.getSoilTest() );
        landDetails.setSoilTestCertificate( landDetailsDto.getSoilTestCertificate() );
        landDetails.setGeoTag( landDetailsDto.getGeoTag() );
        landDetails.setLatitude( landDetailsDto.getLatitude() );
        landDetails.setLongitude( landDetailsDto.getLongitude() );
        landDetails.setIrrigationSource( landDetailsDto.getIrrigationSource() );
        landDetails.setBorewellDischarge( landDetailsDto.getBorewellDischarge() );
        landDetails.setBorewellLocation( landDetailsDto.getBorewellLocation() );

        return landDetails;
    }
}
