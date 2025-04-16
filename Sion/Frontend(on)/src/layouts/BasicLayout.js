import React, { useCallback, useState } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../slices/authSlice";
import UserInfo from "../components/UserInfo";

const BasicLayout = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { userId, userRole } = useSelector((state) => state.auth);
  const [openEnrollmentMenu, setOpenEnrollmentMenu] = useState(false);
  const [openGradeMenu, setOpenGradeMenu] = useState(false);
  const [openInfoMenu, setOpenInfoMenu] = useState(false);

  const handleLogout = useCallback(() => {
    dispatch(logout());
    localStorage.removeItem("id");
    localStorage.removeItem("pw");
    localStorage.removeItem("role");
    navigate("/member/login");
  }, [dispatch, navigate]);

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      <header className="bg-white shadow-lg hover:shadow-xl transition-shadow duration-300 p-4 flex justify-between items-center bg-opacity-80">
        <Link to="/main" className="flex items-center space-x-3">
          <img src="/images/eonLogo.jpg" alt="학교 로고" className="h-16" />
          <span className="text-2xl font-extrabold text-blue-800">
            <ul>
              <li> 이온대학교 </li>
              <li> EON UNIVERSITY </li>
            </ul>
          </span>
        </Link>
        <div className="flex items-center space-x-4">
          <UserInfo />
          {userId ? (
            <button
              onClick={handleLogout}
              className="bg-blue-500 hover:bg-blue-900 text-white text-sm font-semibold py-1 px-4 rounded transition"
            >
              로그아웃
            </button>
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

      <div className="flex flex-1">
        <nav className="bg-blue-800 text-white w-64 p-4 flex flex-col min-h-full">
          <div className="flex flex-col space-y-8">
            {userRole === "STUDENT" && (
              <>
                <Link
                  to="/main/noticelist"
                  className="hover:bg-blue-500 px-6 py-3 mt-10"
                >
                  공지사항 ▶
                </Link>

                {/* 정보조회 */}
                <div className="flex flex-col mt-5 mb-5">
                  <button
                    className="hover:bg-blue-500 px-6 py-3 text-left w-full"
                    onClick={() => setOpenInfoMenu((prev) => !prev)}
                  >
                    정보조회 {openInfoMenu ? "▼" : "▶"}
                  </button>
                  <div
                    className={`pl-8 overflow-hidden transition-all duration-700 ease-in-out ${
                      openInfoMenu
                        ? "max-h-60 opacity-100 mt-5"
                        : "max-h-0 opacity-0"
                    } space-y-3 text-sm`}
                  >
                    {openInfoMenu && (
                      <>
                        <Link
                          to="/main/student"
                          className="hover:underline block pt-3"
                        >
                          🙍‍♂️ 내 정보 조회
                        </Link>
                        <Link
                          to="/main/password"
                          className="hover:underline block pt-6"
                        >
                          🔐 비밀번호 변경
                        </Link>
                      </>
                    )}
                  </div>
                </div>

                {/* 수강 신청 */}
                <div className="flex flex-col mt-2 mb-2">
                  <button
                    className="hover:bg-blue-500 px-6 py-3 text-left w-full"
                    onClick={() => setOpenEnrollmentMenu((prev) => !prev)}
                  >
                    수강 신청 {openEnrollmentMenu ? "▼" : "▶"}
                  </button>
                  <div
                    className={`pl-8 overflow-hidden transition-all duration-700 ease-in-out ${
                      openEnrollmentMenu
                        ? "max-h-60 opacity-100 mt-5"
                        : "max-h-0 opacity-0"
                    } space-y-3 text-sm`}
                  >
                    {openEnrollmentMenu && (
                      <>
                        <Link
                          to="/main/enrollment"
                          className="hover:underline block pt-3"
                        >
                          📋 수강신청 목록
                        </Link>
                        <Link
                          to="/main/history"
                          className="hover:underline block pt-6"
                        >
                          🗂 수강신청 내역
                        </Link>
                        <Link
                          to="/main/schedule"
                          className="hover:underline block pt-6"
                        >
                          ⏰ 시간표 조회
                        </Link>
                      </>
                    )}
                  </div>
                </div>

                {/* 성적 조회 */}
                <div className="flex flex-col mt-2 mb-2">
                  <button
                    className="hover:bg-blue-500 px-6 py-3 text-left w-full"
                    onClick={() => setOpenGradeMenu((prev) => !prev)}
                  >
                    성적 조회 {openGradeMenu ? "▼" : "▶"}
                  </button>
                  <div
                    className={`pl-8 overflow-hidden transition-all duration-700 ease-in-out ${
                      openGradeMenu
                        ? "max-h-40 opacity-100 mt-5"
                        : "max-h-0 opacity-0"
                    } space-y-3 text-sm`}
                  >
                    {openGradeMenu && (
                      <>
                        <Link
                          to="/main/grades"
                          className="hover:underline block pt-3"
                        >
                          📄 전체성적 조회
                        </Link>
                        <Link
                          to="/main/currentgrades"
                          className="hover:underline block pt-6"
                        >
                          📑 현재학기 성적 조회
                        </Link>
                      </>
                    )}
                  </div>
                </div>

                <Link
                  to="/main/qnalist"
                  className="hover:bg-blue-500 px-6 py-3"
                >
                  Q&A ▶
                </Link>
              </>
            )}

            {userRole === "ADMIN" && (
              <>
                <Link
                  to="/main/noticelist"
                  className="hover:bg-blue-500 px-6 py-3 mt-6"
                >
                  공지사항 ▶
                </Link>
                <Link
                  to="/main/admin/user-list"
                  className="hover:bg-blue-500 px-6 py-3 mt-6"
                >
                  사용자 관리 ▶
                </Link>
                <Link to="/main/users" className="hover:bg-blue-500 px-6 py-3">
                  일정 관리 ▶
                </Link>
                <Link
                  to="/main/classes"
                  className="hover:bg-blue-500 px-6 py-3"
                >
                  강의 관리 ▶
                </Link>
                <Link
                  to="/main/qnalist"
                  className="hover:bg-blue-500 px-6 py-3"
                >
                  Q&A ▶
                </Link>
              </>
            )}

            {userRole === "PROFESSOR" && (
              <>
                <Link
                  to="/main/noticelist"
                  className="hover:bg-blue-500 px-6 py-3 mt-6"
                >
                  공지사항 ▶
                </Link>
                <Link
                  to="/main/professor/classes"
                  className="hover:bg-blue-500 px-6 py-3 mt-6"
                >
                  강의 관리 ▶
                </Link>
                <Link
                  to="/main/professor/grades"
                  className="hover:bg-blue-500 px-6 py-3"
                >
                  성적 관리 ▶
                </Link>
              </>
            )}
          </div>
        </nav>

        <main className="p-8 bg-gray-50 shadow-md w-full flex-1 pb-16">
          <Outlet />
        </main>
      </div>

      <footer className="bg-blue-900 text-white text-center py-4 text-sm w-full mt-auto">
        <div>
          01212 서울특별시 한국구 한국산로 12(한국동) 이온대학교 02-123-1234
          <br />
          eonuniversity@naver.com
          <br />
          COPYRIGHT © EON UNIVERSITY. ALL RIGHTS RESERVED.
        </div>
      </footer>
    </div>
  );
};

export default BasicLayout;
