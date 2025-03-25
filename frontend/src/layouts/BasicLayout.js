import React, { useCallback, useContext } from "react";
import { Link, Outlet } from "react-router-dom";
import UserInfo from "../components/UserInfo";
import { AuthContext } from "../App";

const BasicLayout = () => {
  
  const { userId, setUserId, userRole } = useContext(AuthContext);
  
  const handleLogout = useCallback(() => {
    setUserId(null);
    localStorage.removeItem("id");
    localStorage.removeItem("pw");
    localStorage.removeItem("role");
  }, [setUserId]);
  
  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      {/* 상단 헤더 */}
      <header className="bg-white shadow-lg hover:shadow-xl transition-shadow duration-300 p-4 flex justify-between items-center bg-opacity-80">
        <Link to="/main" className="flex items-center space-x-3">
          <img src="/images/eonLogo.jpg" alt="학교 로고" className="h-16" />
          <span className="text-lg font-extrabold text-blue-800">
            이온 학사관리 시스템
          </span>
        </Link>

        <div className="flex items-center space-x-4">
          <UserInfo />
          {userId ? (
            <Link
              to="/"
              className="bg-blue-500 hover:bg-blue-900 text-white text-sm font-semibold py-1 px-4 rounded transition"
              onClick={handleLogout}
            >
              로그아웃
            </Link>
          ) : (
            <Link
              to="/member/login"
              className="bg-blue-500 hover:bg-blue-900 text-white text-sm font-semibold py-1 px-4 rounded transition"
            >
              로그인
            </Link>
          )}
        </div>
      </header>

      {/* 메인 컨테이너 (사이드바 & 콘텐츠) */}
      <div className="flex flex-1">
        {/* 왼쪽 사이드바 */}
        <nav className="bg-blue-800 text-white w-64 p-4 flex flex-col min-h-full">
          <div className="flex flex-col space-y-8">
            {userRole === "STUDENT" && (
              <>
                <Link to="/main/student" className="hover:bg-blue-500 px-6 py-3 mt-6">정보조회 ▶</Link>
                <Link to="/main/courses" className="hover:bg-blue-500 px-6 py-3">강의목록 ▶</Link>
                <Link to="/main/enrollment" className="hover:bg-blue-500 px-6 py-3">수강신청 ▶</Link>
                <Link to="/main/schedule" className="hover:bg-blue-500 px-6 py-3">시간표 조회 ▶</Link>
                <Link to="/main/grades" className="hover:bg-blue-500 px-6 py-3">성적조회 ▶</Link>
                <Link to="/main/qnalist" className="hover:bg-blue-500 px-6 py-3">Q&A ▶</Link>
              </>
            )}
            {userRole === "ADMIN" && (
              <>
                <Link to="/main/admin" className="hover:bg-blue-500 px-6 py-3 mt-6">관리자 대시보드 ▶</Link>
                <Link to="/main/users" className="hover:bg-blue-500 px-6 py-3">사용자 관리 ▶</Link>
                <Link to="/main/classes" className="hover:bg-blue-500 px-6 py-3">강의 관리 ▶</Link>
                <Link to="/main/qnalist" className="hover:bg-blue-500 px-6 py-3">Q&A ▶</Link>
              </>
            )}
          </div>
        </nav>

        {/* 오른쪽 콘텐츠 영역 */}
        <main className="p-8 bg-gray-50 shadow-md w-full flex-1 pb-16">
          <Outlet />
        </main>
      </div>

      {/* 하단 푸터 */}
      <footer className="bg-blue-900 text-white text-center py-4 text-sm w-full mt-auto">
        <div>
          01212 서울특별시 한국구 한국산로 12(한국동) 이온대학교 02-123-1234<br />
          webmaster@eon.ac.kr<br />
          COPYRIGHT © EON UNIVERSITY.ALL RIGHTS RESERVED.
        </div>
      </footer>
    </div>
  );
};

export default BasicLayout;