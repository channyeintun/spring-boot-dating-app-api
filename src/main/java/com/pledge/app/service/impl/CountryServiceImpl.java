package com.pledge.app.service.impl;

import com.pledge.app.dao.CountryDAO;
import com.pledge.app.entity.Country;
import com.pledge.app.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDAO countryDAO;

    @Override
    public Country save(Country country) {
        return countryDAO.save(country);
    }

    @Override
    public List<Country> findAll() {
        return countryDAO.findAll();
    }

    @Override
    public void delete(Long id) {
        try{
            countryDAO.deleteCountry(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
