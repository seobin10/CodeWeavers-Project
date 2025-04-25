package com.cw.cwu.service.admin;

import com.cw.cwu.domain.BuildingStatus;
import com.cw.cwu.dto.BuildingDTO;

import java.util.List;

public interface AdminBuildingService {
    void createBuilding(String name, BuildingStatus status);
    List<BuildingDTO> getAllBuildings();
    void updateBuilding(Integer buildingId, String newName, BuildingStatus newStatus);
    void deleteBuilding(Integer buildingId);
}