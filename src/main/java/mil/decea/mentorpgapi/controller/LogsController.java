package mil.decea.mentorpgapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLogRecord;
import mil.decea.mentorpgapi.domain.changewatch.logs.RequestLogsRecord;
import mil.decea.mentorpgapi.domain.daoservices.ChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/logs")
@SecurityRequirement(name = "bearer-key")
@Secured({"ADMIN","COORDENADOR","SECRETARIA"})
public class LogsController {
    private final ChangeLogService changeLogService;

    @Autowired
    public LogsController(ChangeLogService changeLogService) {
        this.changeLogService = changeLogService;

    }
    @PostMapping("/searchLogs")
    public ResponseEntity<Page<ChangeLogRecord>> searchLogs(@RequestBody RequestLogsRecord req) {
        return ResponseEntity.ok(changeLogService.getAllLogsFor(req, PageRequest.of(req.page(), req.size())));
    }

}
