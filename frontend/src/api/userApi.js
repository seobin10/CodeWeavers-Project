import axios from "axios";

export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/user`;
export const getOne = async (userId) => {
    const res = await axios.get(`${prefix}/${userId}`);
    return res.data;
  };
