package com.cw.cwu.service.user;

import com.cw.cwu.domain.Semester;
import com.cw.cwu.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserSemesterServiceImpl implements UserSemesterService {

    private final SemesterRepository semesterRepository;

    // 현재 학기중인지 확인
    @Override
    public Semester getCurrentSemester() {
        LocalDate now = LocalDate.now();
        return semesterRepository.findCurrentSemester(now)
                .orElseThrow(() -> new IllegalArgumentException("현재 학기를 찾을 수 없습니다."));
    }
}
