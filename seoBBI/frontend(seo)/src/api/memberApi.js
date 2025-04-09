import axios from "axios";
import { getAuthHeader } from "../util/authHeader"; // 인증 헤더 유틸 임포트

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/user`;

// ✅ 로그인 (인증 필요 없음)
export const loginPost = async (loginParam) => {
  try {
    const res = await axios.post(`${prefix}/login`, loginParam, {
      headers: { "Content-Type": "application/json" },
    });
    return res.data;
  } catch (error) {
    console.error("로그인 요청 실패:", error);
    return { error: "로그인 실패. 다시 시도해주세요." };
  }
};

// ✅ 사용자 정보 조회 (인증 필요)
export const fetchUserInfo = (id) => {
  console.log("id: ", id);
  return axios.get(`${prefix}/${id}`, getAuthHeader());
};

// ✅ 사용자 정보 수정 (인증 필요 + 예외 처리 보완)
export const updateUserInfo = async (userData) => {
  try {
    if (!userData?.userId) throw new Error("Invalid userId!");

    const headers = getAuthHeader(); // 인증 헤더를 try 안에서 받아야 안전

    const res = await axios.put(
      `${prefix}/${userData.userId}/update`,
      userData,
      headers
    );
    return res.data;
  } catch (error) {
    console.error("유저 정보 업데이트 실패:", error);
    return { error: "정보 업데이트 실패. 다시 시도해주세요." };
  }
};

// ✅ 학번 찾기 (인증 필요 없음)
export const findUserId = async (formData) => {
  try {
    console.log("formData", formData);
    const res = await axios.post(`${prefix}/finduserId`, formData, {
      headers: { "Content-Type": "application/json" },
    });
    return res.data;
  } catch (error) {
    console.error("학번 찾기 실패:", error);
    return { error: "학번 찾기에 실패했습니다." };
  }
};
