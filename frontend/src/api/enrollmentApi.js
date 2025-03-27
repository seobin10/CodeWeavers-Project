// src/api/enrollmentApi.js
import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/students/enrollment`;

export const getFilters = () => {
  return Promise.all([
    axios.get(`${prefix}/departments`),
    axios.get(`${prefix}/courseTypes`),
    axios.get(`${prefix}/courseYears`),
    axios.get(`${prefix}/classDays`),
    axios.get(`${prefix}/classTimes`),
    axios.get(`${prefix}/credits`),
  ]);
};

// 수강 신청 가능한 강의 목록 (검색)
export const searchCourses = (userId, params) => {
  return axios.get(`${prefix}/${userId}/enrollment`, { params });
};

// 수강 신청 (담기)
export const enrollCourse = (userId, data) => {
  return axios.post(`${prefix}/${userId}/enrollment`, data);
};

// 수강 신청 내역 조회
export const getEnrolledCourses = (userId) => {
  return axios.get(`${prefix}/${userId}/history`);
};

// 수강 신청 내역에서 강의 삭제
export const deleteCourse = (userId, classId) => {
  return axios.delete(`${prefix}/${userId}/course/${classId}`);
};
