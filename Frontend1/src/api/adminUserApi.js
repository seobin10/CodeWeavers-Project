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

export const getAllUsers = (
  page = 1,
  size = 10,
  keyword = "",
  sortField = "userId",
  sortDir = "asc" 
) => {
  const safeSize = isNaN(Number(size)) || Number(size) <= 0 ? 10 : Number(size);
  return axios.get(`${prefix}/users`, {
    params: {
      page,
      size: safeSize,
      keyword,
      sortField,
      sortDir, 
    },
  });
};

export const deleteUser = (userId) => axios.delete(`${prefix}/users/${userId}`);
