package com.cw.cwu.util;

import com.cw.cwu.dto.UserCreateRequestDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelParserUtil {

    public static List<UserCreateRequestDTO> parseUserExcel(MultipartFile file) throws Exception {
        List<UserCreateRequestDTO> userList = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // 헤더 스킵함

            UserCreateRequestDTO dto = new UserCreateRequestDTO();
            dto.setUserId(getStringValue(row.getCell(0)));
            dto.setUserName(getStringValue(row.getCell(1)));
            dto.setUserBirth(getLocalDateValue(row.getCell(2)));
            dto.setDepartmentId(Integer.parseInt(getStringValue(row.getCell(3))));
            dto.setUserRole(Enum.valueOf(com.cw.cwu.domain.UserRole.class, getStringValue(row.getCell(4))));

            // 비밀번호는 생년월일 기준 초기 비번 자동 생성
            String birth = dto.getUserBirth().toString();
            String defaultPassword = birth.substring(2, 4) + birth.substring(5, 7) + birth.substring(8, 10) + "!";
            dto.setUserPassword(defaultPassword);

            dto.setUserImgUrl("/uploads/profiles/" + dto.getUserId() + "_profile.jpg");

            userList.add(dto);
        }

        workbook.close();
        return userList;
    }

    private static String getStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue().trim();
        }
    }


    private static LocalDate getLocalDateValue(Cell cell) {
        if (cell == null) {
            throw new RuntimeException("생년월일 칸은 비어 있을 수 없습니다.");
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            return LocalDate.parse(cell.getStringCellValue().trim());
        } else {
            throw new RuntimeException("생년월일 칸의 타입이 올바르지 않습니다.");
        }
    }


}
