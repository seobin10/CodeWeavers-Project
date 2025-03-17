import React from "react";
import { Link, Outlet } from "react-router-dom";
import UserInfo from "../components/UserInfo";

const cleanLocal = function () {
  localStorage.removeItem("id");
  localStorage.removeItem("pw");
};
const BasicLayout = () => {
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
          {localStorage.getItem("id") != null ? (
            <Link
              to="/"
              button
              className="bg-gray-700 hover:bg-gray-900 text-white text-sm font-semibold py-1 px-3 rounded transition"
              onClick={cleanLocal}
            >
              로그아웃
            </Link>
          ) : (
            <Link
              to="/"
              button
              className="bg-gray-700 hover:bg-gray-900 text-white text-sm font-semibold py-1 px-3 rounded transition"
              onClick={cleanLocal}
            >
              로그인
            </Link>
          )}
        </div>
      </header>

      {/* 메인 레이아웃 */}
      <div className="min-h-screen flex flex-col bg-gray-100">
        {/* 네비게이션 바 */}
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
            수강신청
          </Link>
          <Link
            to="/main/grades"
            className="hover:bg-blue-700 px-4 py-2 rounded"
          >
            성적조회
          </Link>
        </nav>

        {/* 메인 컨텐츠 */}
        <main className="flex-1 mx-auto p-6 bg-white shadow-md mt-4 rounded-md w-full max-w-7xl">
          <Outlet />
        </main>

        {/* 푸터 */}
        <footer className="bg-gray-800 text-white text-center py-4 text-sm mt-auto">
          © 2025 CWU | 학사관리 시스템
        </footer>
      </div>
    </>
  );
};

export default BasicLayout;
