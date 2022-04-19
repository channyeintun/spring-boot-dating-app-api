package com.pledge.app.endpoint;

import com.pledge.app.entity.Country;
import com.pledge.app.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/country")
public class CountryEndpoint {

    @Autowired
    CountryService countryService;

    //    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVELOPER"})
    @PostMapping
    public ResponseEntity<?> saveNewCountry(@RequestBody Country country){
        return ResponseEntity.ok(countryService.save(country));
    }

    @GetMapping
    public ResponseEntity<?> getAllCountry(){
        return ResponseEntity.ok(countryService.findAll());
    }

    //    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVELOPER"})
    @PutMapping
    public ResponseEntity<?> updateCountry(@RequestBody Country country){
        return ResponseEntity.ok(countryService.save(country));
    }
}
