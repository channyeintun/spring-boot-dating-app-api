package com.pledge.app.dao;

import com.pledge.app.entity.Country;
import com.pledge.app.repository.readOnly.CountryReadOnlyRepository;
import com.pledge.app.repository.readWrite.CountryReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryDAO {
    private CountryReadWriteRepository countryReadWriteRepository;
    private CountryReadOnlyRepository countryReadOnlyRepository;

    @Autowired
    public CountryDAO(CountryReadWriteRepository countryReadWriteRepository,
                      CountryReadOnlyRepository countryReadOnlyRepository){
        this.countryReadOnlyRepository=countryReadOnlyRepository;
        this.countryReadWriteRepository=countryReadWriteRepository;
    }

    public Country save(Country country){
        return this.countryReadWriteRepository.save(country);
    }

    public List<Country> findAll(){
        return this.countryReadOnlyRepository.findAll();
    }

    public void deleteCountry(Long id){
        this.countryReadWriteRepository.deleteById(id);
    }
}
