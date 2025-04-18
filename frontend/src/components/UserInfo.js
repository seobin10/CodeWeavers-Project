  import React, { useEffect, useState, useMemo } from "react";
  import { useSelector } from "react-redux";
  import { fetchUserInfo } from "../api/memberApi";
  import { fetchStudentStatus } from "../api/studentApi";

  const UserInfo = () => {
    const { userId, userRole } = useSelector((state) => state.auth);
    const [userInfo, setUserInfo] = useState(null);
    const [statusInfo, setStatusInfo] = useState(null);
    const [isLoading, setIsLoading] = useState(true); // 🔥 추가

    useEffect(() => {
      const idToUse = userId || localStorage.getItem("id");
      if (!idToUse || idToUse === "null" || idToUse === "undefined") return;

      getUserInfo(idToUse);
    }, [userId]);

    const getUserInfo = async (id) => {
      try {
        const response = await fetchUserInfo(id);
        setUserInfo(response.data);

        if (userRole === "STUDENT") {
          const statusRes = await fetchStudentStatus(id);
          setStatusInfo(statusRes.data);
        }
      } catch (error) {
        console.error("Error fetching user info or status:", error);
      } finally {
        setIsLoading(false); // 🔥 둘 다 끝났을 때 로딩 false
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

    // 🔥 여기: 둘 다 준비 안 됐으면 아예 렌더 안 해
    if (isLoading || !processedUserInfo) {
      return <div className="text-gray-400 text-sm">불러오는 중...</div>;
    }

    return (
      <div className="text-sm text-gray-600 flex flex-wrap items-center">
        <img
          src={`http://localhost:8080${userInfo.userImgUrl}`}
          alt="프로필 이미지"
          className="w-[45px] h-[45px] object-cover rounded-full mr-4"
        />

        <span>
          <span className="font-semibold">성명:</span>{" "}
          {processedUserInfo.userName} ({processedUserInfo.userId})
        </span>

        {(userRole === "STUDENT" || userRole === "PROFESSOR") && (
          <>
            <span className="mx-3 text-gray-400">|</span>
            <span>
              <span className="font-semibold">학과:</span>{" "}
              {processedUserInfo.departmentName || "N/A"}
            </span>
          </>
        )}

        {userRole === "STUDENT" && statusInfo && (
          <>
            <span className="mx-3 text-gray-400">|</span>
            <span>
              <span className="font-semibold">학적상태:</span>{" "}
              {statusInfo.graduationEligible
                ? "졸업"
                : `재학 (${statusInfo.studentYear}학년)`}
            </span>
          </>
        )}
      </div>
    );
  };

  export default UserInfo;
