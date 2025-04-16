import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/admin/grades`;

// 📌 현재 학기의 학과별 성적 집계 실행
export const finalizeGradesByDepartment = (departmentId) => {
  return axios.post(
    `${prefix}/finalize?departmentId=${departmentId}`,
    {}, // POST지만 바디 없음
    getAuthHeader()
  );
};

// 📌 현재 학기의 학과별 성적 상태 요약 조회
export const getGradeStatusSummary = (departmentId) => {
  return axios.get(
    `${prefix}/summary?departmentId=${departmentId}`,
    getAuthHeader()
  );
};

// 📌 현재 학기 조회
export const getCurrentSemester = () => {
  return axios.get(`${API_SERVER_HOST}/api/admin/grades/current-semester`, getAuthHeader());
};
