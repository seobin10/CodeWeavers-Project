import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/admin`;

export const createUser = (userData) => {
  return axios.post(`${prefix}/users`, userData);
};

export const getDepartments = () => {
    return axios.get(`${prefix}/departments`);
};