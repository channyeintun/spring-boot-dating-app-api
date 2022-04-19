package com.pledge.app.dao;

import com.pledge.app.entity.Report;
import com.pledge.app.repository.readOnly.ReportReadOnlyRepository;
import com.pledge.app.repository.readWrite.ReportReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportDAO {
    private ReportReadWriteRepository reportReadWriteRepository;
    private ReportReadOnlyRepository reportReadOnlyRepository;

    @Autowired
    public ReportDAO(ReportReadOnlyRepository reportReadOnlyRepository,
                     ReportReadWriteRepository reportReadWriteRepository) {
        this.reportReadOnlyRepository = reportReadOnlyRepository;
        this.reportReadWriteRepository = reportReadWriteRepository;
    }

    public Report save(Report report) {
        return this.reportReadWriteRepository.saveAndFlush(report);
    }

    public List<Report> getReportsByChecked(boolean checked) {
        return this.reportReadOnlyRepository.findByChecked(checked);
    }
}
