package com.farmer.farmermanagement.repository;
 
import com.farmer.farmermanagement.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    // Add custom methods if needed later
}