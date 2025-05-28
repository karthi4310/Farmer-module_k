package com.farmer.farmermanagement.repository;
 
import com.farmer.farmermanagement.entity.District;
import com.farmer.farmermanagement.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
 
    // Custom method to get all districts by selected state
    List<District> findByState(State state);
}