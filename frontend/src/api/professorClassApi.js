import axios from "axios";
import { getAuthHeader } from "../util/authHeader"; // ✅ 인증 헤더 유틸 임포트

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/professor`;

// ✅ 교수의 강의 목록 조회
export const getMyClasses = (
  professorId,
  page = 1,
  size = 10,
  sortField = "id",
  sortDir = "asc"
) => {
  return axios.get(`${prefix}/classes`, {
    params: {
      professorId,
      page,
      size,
      sortField,
      sortDir,
    },
    ...getAuthHeader(), // ✅ headers 병합
  });
};

// ✅ 강의 등록
export const createClass = (classData) => {
  return axios.post(`${prefix}/classes`, classData, getAuthHeader());
};

// ✅ 강의 수정
export const updateClass = (classData) => {
  return axios.put(`${prefix}/classes`, classData, getAuthHeader());
};

// ✅ 강의 삭제
export const deleteClass = (classId) => {
  return axios.delete(`${prefix}/classes/${classId}`, getAuthHeader());
};

// ✅ 과목 목록 조회
export const getCourses = () => {
  return axios.get(`${prefix}/courses`, getAuthHeader());
};

// ✅ 특정 조건(학기, 요일, 교시) 기준으로 비어있는 강의실만 조회
export const getLectureRooms = ({ semester, day, startTime, endTime }) => {
  return axios.get(`${prefix}/lecture-rooms`, {
    params: { semester, day, startTime, endTime },
    ...getAuthHeader(),
  });
};