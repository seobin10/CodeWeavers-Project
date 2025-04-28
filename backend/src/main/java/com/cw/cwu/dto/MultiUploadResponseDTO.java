package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiUploadResponseDTO {
    private int successCount;
    private int failureCount;
    private List<String> failureDetails;
}