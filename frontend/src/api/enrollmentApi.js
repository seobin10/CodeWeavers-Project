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

export const searchCourses = (userId, params) => {
  const { page, size, ...restParams } = params;

  return axios.get(`${prefix}/${userId}/enrollment`, {
    params: {
      ...restParams,
      page: Number(page),
      size: Number(size),
    },
  });
};

export const enrollCourse = (userId, data) => {
  return axios.post(`${prefix}/${userId}/enrollment`, data);
};

