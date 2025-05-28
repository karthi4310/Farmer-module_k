package com.farmer.farmermanagement.GeoLocationController;

import com.farmer.farmermanagement.entity.Country;
import com.farmer.farmermanagement.repository.CountryRepository;
import com.farmer.farmermanagement.service.CountryStateCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private CountryStateCityService countryService;

    @Autowired
    private CountryRepository countryRepository;

    // 🌍 Fetch and Save Countries from External API
    @PostMapping("/fetch")
    public String fetchAndSaveCountries() {
        countryService.fetchAndSaveCountries();
        return "Countries fetched and saved successfully!";
    }

    // 🌍 Get All Countries
    @GetMapping
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    // 🌍 Get Country by ID
    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
    }

    // 🌍 Create Country manually
    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }

    // 🌍 Update Country
    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable Long id, @RequestBody Country countryDetails) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));

        country.setName(countryDetails.getName());
        country.setIso2(countryDetails.getIso2());
        country.setDialCode(countryDetails.getDialCode());
        country.setActive(countryDetails.isActive());

        return countryRepository.save(country);
    }

    // 🌍 Delete Country
    @DeleteMapping("/{id}")
    public String deleteCountry(@PathVariable Long id) {
        countryRepository.deleteById(id);
        return "Country deleted successfully!";
    }
}
