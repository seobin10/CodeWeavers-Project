import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../App";
import axios from "axios";

const UserInfo = () => {
  const { userId } = useContext(AuthContext);
  const [userInfo, setUserInfo] = useState(null);
  const localId = localStorage.getItem("id");

  useEffect(() => {
    const idToUse = userId || localId;
    if (!idToUse) return;
    fetchUserInfo(idToUse);
  }, [userId, localId]);

  const fetchUserInfo = async (userId) => {
    try {
      console.log("Fetching user info for:", userId);
      const response = await axios.get(
        `http://localhost:8080/api/user/${userId}`
      );

      const filteredData = {
        userId: response.data.userId,
        userName: response.data.userName,
        departmentName: response.data.departmentName,
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
          {userInfo.userId}) |<span className="font-semibold"> 학과:</span>{" "}
          {userInfo.departmentName || "N/A"}
        </>
      ) : (
        "로그인 필요"
      )}
    </div>
  );
};

export default UserInfo;
