import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/admin`;

// 사용자 생성
export const createUser = (userData) => {
  const config = {
    ...getAuthHeader(),
    headers: {
      ...getAuthHeader().headers,
      "Content-Type": "application/json", // ✅ 명시 필요
    },
  };

  return axios.post(`${prefix}/users`, userData, config);
};

// 부서 목록 조회
export const getDepartments = () => {
  return axios.get(`${prefix}/departments`, getAuthHeader());
};

// 프로필 이미지 업로드
export const uploadProfileImage = (formData) => {
  const authHeader = getAuthHeader();

  return axios.post(`${prefix}/profile`, formData, {
    ...authHeader,
    headers: {
      ...authHeader.headers,
      "Content-Type": "multipart/form-data",
    },
  });
};

// 전체 사용자 목록 조회
export const getAllUsers = (
  page = 1,
  size = 10,
  keyword = "",
  sortField = "userId",
  sortDir = "asc"
) => {
  const safeSize = isNaN(Number(size)) || Number(size) <= 0 ? 10 : Number(size);
  return axios.get(`${prefix}/users`, {
    params: {
      page,
      size: safeSize,
      keyword,
      sortField,
      sortDir,
    },
    ...getAuthHeader(), // ✅ headers 병합
  });
};

// 사용자 삭제
export const deleteUser = (userId) =>
  axios.delete(`${prefix}/users/${userId}`, getAuthHeader());

// 사용자 수정
export const updateUser = (userData) => {
  return axios.put(`${prefix}/users`, userData, getAuthHeader());
};

// 비밀번호 초기화
export const resetPassword = (userId) => {
  return axios.put(
    `${prefix}/users/${userId}/reset-password`,
    null, // body 없음 → null 명시
    getAuthHeader() // 세 번째 인자에 headers 설정
  );
};
