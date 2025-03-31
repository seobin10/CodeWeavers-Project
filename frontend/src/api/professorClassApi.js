import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/professor`;

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
  });
};

// 강의 등록
export const createClass = (classData) => {
  return axios.post(`${prefix}/classes`, classData);
};

// 강의 수정

export const updateClass = (classData) => {
  return axios.put(`${prefix}/classes`, classData);
};

// 강의 삭제

export const deleteClass = (classId) => {
  return axios.delete(`${prefix}/classes/${classId}`);
};

// 과목 목록 조회

export const getCourses = () => {
  return axios.get(`${prefix}/courses`);
};

// 강의실 목록 조회

export const getLectureRooms = () => {
  return axios.get(`${prefix}/lecture-rooms`);
};
