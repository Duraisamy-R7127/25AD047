package com.FestPass.Controller;

import com.FestPass.Dto.DashboardResponse;
import com.FestPass.Service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService =
                dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse>
    getDashboard() {

        return ResponseEntity.ok(
                dashboardService.getDashboard());
    }
}