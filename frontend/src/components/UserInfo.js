import React, { useContext, useEffect, useState, useMemo } from "react";
import { AuthContext } from "../App";
import axios from "axios";

const UserInfo = () => {
  const { userId, userRole } = useContext(AuthContext);
  const [userInfo, setUserInfo] = useState(null);
  const localId = localStorage.getItem("id");

  useEffect(() => {
    const idToUse = userId || localId;
    if (!idToUse || idToUse === "null") return;
    fetchUserInfo(idToUse);
  }, [userId, localId]);

  const fetchUserInfo = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/${userId}`
      );
      setUserInfo(response.data);
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };

  const processedUserInfo = useMemo(() => {
    if (!userInfo) return null;
    return {
      userId: userInfo.userId,
      userName: userInfo.userName,
      departmentName: userInfo.departmentName,
    };
  }, [userInfo]);

  return processedUserInfo ? (
    <div className="text-sm text-gray-600">
      <span className="font-semibold">
        성명: {processedUserInfo.userName} ({processedUserInfo.userId})
      </span>
      {(userRole === "STUDENT" || userRole === "PROFESSOR") && (
        <>
          {" | "}
          <span className="font-semibold">
            학과: {processedUserInfo.departmentName || "N/A"}
          </span>
        </>
      )}
    </div>
  ) : (
    "로그인 필요"
  );
};

export default UserInfo;
