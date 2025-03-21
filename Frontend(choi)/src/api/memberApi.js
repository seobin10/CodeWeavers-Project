import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/user`;

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

export const updateUserInfo = async (userData) => {
  try {
    if (!userData?.userId) throw new Error("Invalid userId!");
    const res = await axios.put(
      `${prefix}/${userData.userId}/update`,
      userData
    );
    return res.data;
  } catch (error) {
    console.error("유저 정보 업데이트 실패:", error);
    return { error: "정보 업데이트 실패. 다시 시도해주세요." };
  }
};

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
