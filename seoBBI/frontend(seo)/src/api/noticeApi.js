import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

const API_SERVER_HOST = "http://localhost:8080api/notice";
const prefix = `${API_SERVER_HOST}/api`;


// 공지사항 리스트 가져오기
export const getList = () => {
  return axios.get(`${prefix}/list`, getAuthHeader());
};