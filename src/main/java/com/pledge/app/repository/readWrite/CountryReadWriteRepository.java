package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryReadWriteRepository extends JpaRepository<Country, Long> {
}
