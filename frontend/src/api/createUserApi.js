import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/admin`;

export const createUser = (userData) => {
  return axios.post(`${prefix}/users`, userData);
};

export const getDepartments = () => {
    return axios.get(`${prefix}/departments`);
};

export const uploadProfileImage = (file) => {
  const formData = new FormData();
  formData.append("file", file);
  return axios.post(`${prefix}/profile`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};