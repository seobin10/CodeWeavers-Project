import React, { useEffect, useState, useMemo } from "react";
import { useSelector } from "react-redux";
import axios from "axios";
import { fetchUserInfo } from "../api/memberApi";

const UserInfo = () => {
  const { userId, userRole } = useSelector((state) => state.auth);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const idToUse = userId || localStorage.getItem("id");
    console.log("id:", idToUse);
    if (!idToUse || idToUse === "null" || idToUse === "undefined") return;

    getUserInfo(idToUse);
  }, [userId]);

  const getUserInfo = async () => {
    try {
      const response = await fetchUserInfo(userId);
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
