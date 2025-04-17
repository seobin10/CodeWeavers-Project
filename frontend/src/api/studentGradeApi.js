import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/students`;

// 성적 정보 조회
export const fetchStudentGrades = () => {
  return axios.get(`${prefix}/grade/grade`, getAuthHeader());
};