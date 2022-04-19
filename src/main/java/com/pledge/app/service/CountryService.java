package com.pledge.app.service;

import com.pledge.app.entity.Country;

import java.util.List;

public interface CountryService {
    public Country save(Country country);
    public List<Country> findAll();
    public void delete(Long id);
}
