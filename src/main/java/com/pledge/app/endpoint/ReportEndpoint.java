package com.pledge.app.endpoint;

import com.pledge.app.dto.ReportDto;
import com.pledge.app.payload.ApiResponse;
import com.pledge.app.payload.ReportRequest;
import com.pledge.app.service.ReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportEndpoint {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping
    public ResponseEntity<?> saveReport(@RequestBody ReportRequest request) {
        ReportDto reportDto = mapper.map(request, ReportDto.class);
        reportService.save(reportDto);
        return ResponseEntity.ok(new ApiResponse(true, "Reported successfully."));
    }

    @GetMapping
    public ResponseEntity<?> getReports(@RequestParam("checked") boolean checked) {
        return ResponseEntity.ok(reportService.getReportsByChecked(checked));
    }

    @PutMapping
    public ResponseEntity<?> updateReport(@RequestBody ReportDto request) {
        return ResponseEntity.ok(reportService.save(request));
    }
}
