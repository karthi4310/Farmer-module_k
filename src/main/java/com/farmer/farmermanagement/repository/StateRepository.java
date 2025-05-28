package com.farmer.farmermanagement.repository;
 
import com.farmer.farmermanagement.entity.State;
import com.farmer.farmermanagement.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
 
    // Custom method to get all states by selected country
    List<State> findByCountry(Country country);
}