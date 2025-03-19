import React, { useCallback, useContext } from "react";
import { Link, Outlet } from "react-router-dom";
import UserInfo from "../components/UserInfo";
import { AuthContext } from "../App";

const BasicLayout = () => {
  const { userId, setUserId } = useContext(AuthContext);

  const handleLogout = useCallback(() => {
    setUserId(null);
    localStorage.removeItem("id");
    localStorage.removeItem("pw");
  }, [setUserId]);

  return (
    <>
      {/* 상단 헤더 */}
      <header className="bg-white p-4 flex justify-between items-center w-full mx-auto px-4 sm:px-8 lg:px-16 max-w-7xl">
        <Link to="/main" className="flex items-center space-x-3 cursor-pointer">
          <img src="/eonLogo.jpg" alt="학교 로고" className="h-14" />
          <span className="text-xl font-semibold text-gray-700">
            이온 학사관리 시스템
          </span>
        </Link>
        <div className="flex items-center space-x-6">
          <UserInfo />
          {userId ? (
            <Link
              to="/"
              className="bg-gray-700 hover:bg-gray-900 text-white text-sm font-semibold py-1 px-3 rounded transition"
              onClick={handleLogout}
            >
              로그아웃
            </Link>
          ) : (
            <Link
              to="/"
              className="bg-gray-700 hover:bg-gray-900 text-white text-sm font-semibold py-1 px-3 rounded transition"
            >
              로그인
            </Link>
          )}
        </div>
      </header>

      <div className="min-h-screen flex flex-col bg-gray-100">
        <nav className="bg-blue-600 text-white p-3 flex justify-center space-x-6">
          <Link
            to="/main/student"
            className="hover:bg-blue-700 px-4 py-2 rounded"
          >
            정보조회
          </Link>
          <Link
            to="/main/courses"
            className="hover:bg-blue-700 px-4 py-2 rounded"
          >
            강의목록
          </Link>
          <Link
            to="/main/enrollment"
            className="hover:bg-blue-700 px-4 py-2 rounded"
          >
            수강신청
          </Link>
          <Link
            to="/main/schedule"
            className="hover:bg-blue-700 px-4 py-2 rounded"
          >
            시간표 조회
          </Link>
          <Link
            to="/main/grades"
            className="hover:bg-blue-700 px-4 py-2 rounded"
          >
            성적조회
          </Link>
        </nav>

        <main className="flex-1 mx-auto p-6 bg-white shadow-md mt-4 rounded-md w-full max-w-7xl">
          <Outlet />
        </main>

        <footer className="bg-gray-800 text-white text-center py-4 text-sm mt-auto">
          © 2025 CWU | 학사관리 시스템
        </footer>
      </div>
    </>
  );
};

export default BasicLayout;