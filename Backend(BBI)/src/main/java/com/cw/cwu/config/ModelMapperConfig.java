package com.cw.cwu.config;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.EnrollmentRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // EnrollmentRequestDTO → Enrollment 커스텀 매핑
        modelMapper.typeMap(EnrollmentRequestDTO.class, Enrollment.class).addMappings(mapper -> {
            mapper.map(src -> {
                User user = new User();
                user.setUserId(src.getStudentId());
                return user;
            }, Enrollment::setStudent);

            mapper.map(src -> {
                ClassEntity classEntity = new ClassEntity();
                classEntity.setId(src.getClassId());
                return classEntity;
            }, Enrollment::setEnrolledClassEntity);
        });

        return modelMapper;
    }
}
