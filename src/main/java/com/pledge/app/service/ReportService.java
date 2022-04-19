package com.pledge.app.service;

import com.pledge.app.dto.ReportDto;
import com.pledge.app.entity.Report;

import java.util.List;

public interface ReportService {
    public Report save(ReportDto reportDto);
    public List<ReportDto> getReportsByChecked(boolean checked);
}
