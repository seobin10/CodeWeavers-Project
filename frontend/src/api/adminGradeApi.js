
import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/admin/grades`;

// 📌 학과별 성적 집계 실행
export const finalizeGradesByDepartment = (semesterId, departmentId) => {
  return axios.post(
    `${prefix}/finalize?semesterId=${semesterId}&departmentId=${departmentId}`,
    {}, // POST지만 바디 없음
    getAuthHeader()
  );
};