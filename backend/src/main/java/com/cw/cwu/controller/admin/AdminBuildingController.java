package com.cw.cwu.controller.admin;

import com.cw.cwu.domain.BuildingStatus;
import com.cw.cwu.dto.BuildingDTO;
import com.cw.cwu.service.admin.AdminBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/buildings")
@RequiredArgsConstructor
public class AdminBuildingController {

    private final AdminBuildingService adminBuildingService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBuilding(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status
    ) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("건물 이름은 필수입니다.");
        }
        if (status == null) {
            return ResponseEntity.badRequest().body("상태를 선택해주세요.");
        }

        try {
            adminBuildingService.createBuilding(name, BuildingStatus.valueOf(status));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    int []arr2 = {1,2,3,4};


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BuildingDTO>> getAllBuildings() {
        List<BuildingDTO> result = adminBuildingService.getAllBuildings();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{buildingId}")
    public ResponseEntity<?> updateBuilding(
            @PathVariable Integer buildingId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newStatus
    ) {
        try {
            adminBuildingService.updateBuilding(
                    buildingId,
                    newName,
                    newStatus != null ? BuildingStatus.valueOf(newStatus) : null
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{buildingId}")
    public ResponseEntity<?> deleteBuilding(@PathVariable Integer buildingId) {
        try {
            adminBuildingService.deleteBuilding(buildingId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}