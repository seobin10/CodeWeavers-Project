import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/students`;

// 성적 정보 조회
export const fetchStudentGrades = () => {
  return axios.get(`${prefix}/grade`, getAuthHeader());
};

// 🔥 총 신청학점/이수학점/GPA 조회 (현재 학기 요약)
export const fetchStudentRecord = () => {
  return axios.get(`${prefix}/grade/record`, getAuthHeader());
};