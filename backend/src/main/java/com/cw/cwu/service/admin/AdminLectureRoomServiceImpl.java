package com.cw.cwu.service.admin;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.dto.LectureRoomUsageDTO;
import com.cw.cwu.repository.ClassEntityRepository;
import com.cw.cwu.service.user.UserSemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLectureRoomServiceImpl implements AdminLectureRoomService {

    private final ClassEntityRepository classEntityRepository;
    private final UserSemesterService userSemesterService;

    @Override
    public List<LectureRoomUsageDTO> getCurrentClassesByLectureRoom(Integer roomId) {
        Semester currentSemester = userSemesterService.getCurrentSemester();

        if (currentSemester == null) {
            // 현재 학기 없음 -> 수업 없음 -> 미사용 처리
            return Collections.emptyList();
        }

        List<ClassEntity> classes = classEntityRepository.findByLectureRoom_IdAndSemester_Id(
                roomId, currentSemester.getId()
        );

        return classes.stream()
                .map(c -> LectureRoomUsageDTO.builder()
                        .classId(c.getId())
                        .courseName(c.getCourse().getName())
                        .professorName(c.getProfessor() != null ? c.getProfessor().getUserName() : "(미지정)")
                        .day(c.getDay())
                        .startTime(c.getStartTime())
                        .endTime(c.getEndTime())
                        .build()
                ).collect(Collectors.toList());
    }
}
