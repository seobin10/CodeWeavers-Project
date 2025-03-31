package com.cw.cwu.service.admin;

import com.cw.cwu.dto.*;

public interface AdminUserService {

   /**
    * [사용자 등록]
    * @param dto 사용자 생성 요청 DTO
    * @return 성공 또는 실패 메시지
    */
   String createUser(UserCreateRequestDTO dto);

   /**
    * [사용자 목록 조회 (페이징, 검색)]
    * @param keyword 사용자 ID 또는 이름 검색어
    * @param pageRequestDTO 페이징 및 정렬 정보
    * @return 사용자 목록 DTO 페이징 응답
    */
   PageResponseDTO<UserDTO> getAllUsers(String keyword, PageRequestDTO pageRequestDTO);

   /**
    * [사용자 삭제]
    * (프로필 이미지 파일도 함께 삭제)
    * @param userId 삭제할 사용자 ID
    * @return 성공 또는 실패 메시지
    */
   String deleteUser(String userId);

   /**
    * [사용자 정보 수정]
    * @param dto 사용자 수정 요청 DTO
    * @return 성공 또는 실패 메시지
    */
   String updateUser(UserUpdateRequestDTO dto);

   /**
    * [비밀번호 초기화]
    * 사용자의 비밀번호를 생년월일 기반의 기본값으로 초기화, 형식: yyMMdd!
    * @param userId 비밀번호를 초기화할 사용자 ID
    * @return 성공 또는 실패 메시지
    */
   String resetPassword(String userId);
}