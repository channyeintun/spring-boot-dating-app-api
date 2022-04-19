package com.pledge.app.service.impl;

import com.pledge.app.dao.ReportDAO;
import com.pledge.app.dto.ReportDto;
import com.pledge.app.entity.Report;
import com.pledge.app.service.ReportService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Report save(ReportDto reportDto) {
        return reportDAO.save(mapper.map(reportDto, Report.class));
    }

    @Override
    public List<ReportDto> getReportsByChecked(boolean checked) {
        List<Report> reports=reportDAO.getReportsByChecked(checked);
        Type targetListType = new TypeToken<List<ReportDto>>() {
        }.getType();
        List<ReportDto> reportDtos=mapper.map(reports,targetListType);
        return reportDtos;
    }
}
