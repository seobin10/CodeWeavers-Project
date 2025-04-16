// src/utils/authHeader.js
export const getAuthHeader = () => {
  const token = localStorage.getItem("accessToken");
  console.log("🔐 Loaded token:", token);

  if (!token) {
    console.warn("⚠️ 토큰이 없습니다! 로그인 상태를 확인하세요.");
    return { headers: {} };
  }

  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};
