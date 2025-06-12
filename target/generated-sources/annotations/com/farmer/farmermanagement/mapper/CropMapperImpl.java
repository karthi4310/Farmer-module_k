package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.CropDto;
import com.farmer.farmermanagement.entity.Crop;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T16:48:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class CropMapperImpl implements CropMapper {

    @Override
    public CropDto toDto(Crop crop) {
        if ( crop == null ) {
            return null;
        }

        CropDto.CropDtoBuilder cropDto = CropDto.builder();

        cropDto.photo( crop.getPhoto() );
        cropDto.cropName( crop.getCropName() );
        cropDto.cropType( crop.getCropType() );
        cropDto.areaInAcres( crop.getAreaInAcres() );
        cropDto.surveyNumber( crop.getSurveyNumber() );
        cropDto.soilTest( crop.getSoilTest() );
        cropDto.soilTestCertificate( crop.getSoilTestCertificate() );
        cropDto.irrigationSource( crop.getIrrigationSource() );
        cropDto.geoTag( crop.getGeoTag() );
        cropDto.latitude( crop.getLatitude() );
        cropDto.longitude( crop.getLongitude() );
        cropDto.netIncome( crop.getNetIncome() );

        return cropDto.build();
    }

    @Override
    public Crop toEntity(CropDto cropDTO) {
        if ( cropDTO == null ) {
            return null;
        }

        Crop.CropBuilder crop = Crop.builder();

        crop.photo( cropDTO.getPhoto() );
        crop.cropName( cropDTO.getCropName() );
        crop.cropType( cropDTO.getCropType() );
        crop.areaInAcres( cropDTO.getAreaInAcres() );
        crop.surveyNumber( cropDTO.getSurveyNumber() );
        crop.soilTest( cropDTO.getSoilTest() );
        crop.soilTestCertificate( cropDTO.getSoilTestCertificate() );
        crop.irrigationSource( cropDTO.getIrrigationSource() );
        crop.geoTag( cropDTO.getGeoTag() );
        crop.latitude( cropDTO.getLatitude() );
        crop.longitude( cropDTO.getLongitude() );
        crop.netIncome( cropDTO.getNetIncome() );

        return crop.build();
    }
}
