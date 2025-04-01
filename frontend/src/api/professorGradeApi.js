import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/professor`;

// 강의별 성적 조회
export const getGradesByClass = (classId) => {
  return axios.get(`${prefix}/grades/class/${classId}`);
};

// 성적 등록
export const registerGrade = (gradeData) => {
  return axios.post(`${prefix}/grades`, gradeData);
};

// 성적 수정
export const updateGrade = (gradeData) => {
  return axios.put(`${prefix}/grades`, gradeData);
};

// 성적 삭제
export const deleteGrade = (gradeId) => {
  return axios.delete(`${prefix}/grades/${gradeId}`);
};
