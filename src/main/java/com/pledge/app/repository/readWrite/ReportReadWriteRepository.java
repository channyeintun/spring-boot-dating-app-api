package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportReadWriteRepository extends JpaRepository<Report, Long> {
}
