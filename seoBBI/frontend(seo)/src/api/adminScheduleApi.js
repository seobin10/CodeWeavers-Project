import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/admin/schedule`;

// 일정 조회 (타입별)
export const getScheduleByType = (type) => {
  return axios.get(`${prefix}/${type}`, getAuthHeader());
};

// 일정 저장 또는 수정
export const saveSchedule = (scheduleData) => {
  return axios.post(`${prefix}`, scheduleData, getAuthHeader());
};

// 오픈 여부 확인 (선택적으로 사용 가능)
export const isScheduleOpen = (type) => {
  return axios.get(`${prefix}/${type}/is-open`, getAuthHeader());
};