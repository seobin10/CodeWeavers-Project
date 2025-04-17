import axios from "axios"
import { getAuthHeader } from "../util/authHeader";
const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/students/evaluation`;

// 강의 평가 질문 불러오기
export const getQuestions = () => {
    return axios.get(`${prefix}/quelist`);
}

// 강의 평가 할 리스트 불러오기
export const getList = (id) => {
    return axios.get(`${prefix}/courselist?studentId=${id}`)
}

// 강의 평가 참여 상태 불러오기
export const getStatus = (id) => {
    return axios.get(`${prefix}/lecturelist?studentId=${id}`)
}

// 강의 평가 제출
export const submitAnswer = (userId, questionId, classId, data) => {
    const url = `${prefix}/savedata?userId=${userId}&questionId=${questionId}&classId=${classId}`;
    return axios.post(url, data, getAuthHeader());
  };
  