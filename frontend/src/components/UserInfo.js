import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../App";

const UserInfo = () => {
  const { userId } = useContext(AuthContext);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    if (!userId) return;
    fetchUserInfo(userId);
  }, [userId]);

  const fetchUserInfo = async (userId) => {
    try {
      console.log("Fetching user info for:", userId);
      
      const response = await fetch(`http://localhost:8080/api/user/${userId}`);
      if (!response.ok) throw new Error("사용자 정보를 불러오지 못했습니다.");
      const data = await response.json();
      
      const filteredData = {
        userId: data.userId,
        userName: data.userName,
        departmentName: data.departmentName, 
      };

      setUserInfo(filteredData);
      
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };

  return (
    <div className="text-sm text-gray-600">
      {userInfo ? (
        <>
          <span className="font-semibold">성명:</span> {userInfo.userName} (
          {userInfo.userId}) | 
          <span className="font-semibold"> 학과:</span> {userInfo.departmentName || "N/A"}
        </>
      ) : (
        "로그인 필요"
      )}
    </div>
  );
};

export default UserInfo;
