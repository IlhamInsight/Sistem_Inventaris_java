package com.bangone.erm.controllers.transaction;

import com.bangone.erm.models.transaction.ApprovalLog;
import com.bangone.erm.services.transaction.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/approvals")
public class ApprovalLogController {
    @Autowired private ApprovalLogService service;
    @GetMapping public List<ApprovalLog> getAll() { return service.getAll(); }
    @PostMapping public ApprovalLog save(@RequestBody ApprovalLog d) { return service.save(d); }
}