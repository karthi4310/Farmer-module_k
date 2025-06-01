package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-30T17:37:24+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );
        userDTO.setPassword( user.getPassword() );
        if ( user.getDateOfBirth() != null ) {
            userDTO.setDateOfBirth( DateTimeFormatter.ISO_LOCAL_DATE.format( user.getDateOfBirth() ) );
        }
        userDTO.setGender( user.getGender() );
        userDTO.setCountry( user.getCountry() );
        userDTO.setState( user.getState() );
        userDTO.setPinCode( user.getPinCode() );
        userDTO.setTimeZone( user.getTimeZone() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( userDto.getFirstName() );
        user.lastName( userDto.getLastName() );
        user.email( userDto.getEmail() );
        user.phoneNumber( userDto.getPhoneNumber() );
        user.password( userDto.getPassword() );
        if ( userDto.getDateOfBirth() != null ) {
            user.dateOfBirth( LocalDate.parse( userDto.getDateOfBirth() ) );
        }
        user.gender( userDto.getGender() );
        user.country( userDto.getCountry() );
        user.state( userDto.getState() );
        user.pinCode( userDto.getPinCode() );
        user.timeZone( userDto.getTimeZone() );

        return user.build();
    }
}
