import React, { useState, useEffect, useCallback } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { useSelector } from "react-redux";
import AlertModal from "../components/AlertModal";
import { getAuthHeader } from "../util/authHeader";

const NoticeDataPage = () => {
  const navigate = useNavigate();
  const [alertModalOpen, setAlertModalOpen] = useState(false);
  const [type, setType] = useState("");
  const [msg, setMsg] = useState("");
  const [goTarget, setGoTarget] = useState(null);

  const location = useLocation();
  const noticeId = location.state?.noticeId;
  const currentPage = location.state?.page;
  const userId = useSelector((state) => state.auth?.userId);
  const userRole = useSelector((state) => state.auth?.userRole);

  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState(null);

  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    const localNoticeId = localStorage.getItem("No.");

    if (noticeId) {
      localStorage.setItem("No.", noticeId);
      fetchContentInfo(noticeId);
    } else if (localNoticeId) {
      fetchContentInfo(localNoticeId);
    }
  }, [noticeId, userId]);

  const fetchContentInfo = async (id) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/notice/${id}`,
        getAuthHeader()
      );
      setContentInfo(response.data);
      handleView();
    } catch (error) {
      setMessage("게시물 정보를 불러올 수 없습니다.");
    }
  };

  // 모달을 일괄 설정하기 위한 메서드, 곧 사용할 예정
  const setAlertData = (modalType, modalMsg, target = null) => {
    setType(modalType);
    setMsg(modalMsg);
    setGoTarget(target); // navigate 정보 저장
    setAlertModalOpen(true);
  };

  const handleClose = () => {
    setAlertModalOpen(false);
    if (goTarget) {
      navigate("/main/noticelist", {
        state: goTarget,
      });
      setGoTarget(null); // navigate 초기화
    }
  };

  // 조회수 증가
  const handleView = useCallback(async () => {
    try {
      await axios.put(
        `http://localhost:8080/api/notice/update/${noticeId}`,
        null,
        getAuthHeader()
      );
      console.log("조회수 증가 성공");
    } catch (error) {
      console.log("조회수 증가 실패");
    }
  }, [noticeId]);

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      {message && <p className="text-red-500 text-center">{message}</p>}
      <h1 className=" font-bold text-left mb-6 ml-6">공지사항</h1>
      {contentInfo ? (
        <div>
          <table className="table-auto border-collapse w-full">
            <tr>
              <td className="px-4 py-2">
                <div className="flex justify-between items-center relative">
                  <span className="text-blue-800 text-5xl font-semibold">
                    {contentInfo.title}
                  </span>
                  <div className="relative">
                    <button
                      onClick={() => setMenuOpen(!menuOpen)}
                      className="text-gray-700 hover:text-gray-950 text-xl px-2"
                    >
                      ⋮
                    </button>
                    {menuOpen && (
                      <div className="absolute right-0 mt-2 w-24 border rounded shadow z-10 text-sm">
                        <Link
                          to={userRole === "ADMIN" ? "/main/noticedelete" : ""}
                          onClick={
                            userRole === "ADMIN"
                              ? undefined
                              : () => {
                                  // showModal("관리자만 삭제할 수 있습니다!");
                                }
                          }
                          state={{ noticeId }}
                          className="block px-4 py-2 hover:bg-gray-100 text-gray-800 border border-black-300"
                        >
                          삭제
                        </Link>
                        <Link
                          to={userRole === "ADMIN" ? "/main/noticeedit" : ""}
                          onClick={
                            userRole === "ADMIN"
                              ? undefined
                              : () => {
                                  // showModal("관리자만 수정할 수 있습니다!");
                                }
                          }
                          state={{ noticeId }}
                          className="block px-4 py-2 hover:bg-gray-100 text-gray-800 border border-black-300 border-t-0"
                        >
                          수정
                        </Link>
                      </div>
                    )}
                  </div>
                </div>
              </td>
            </tr>
            <tr></tr>
            <tr>
              <td className="px-4 pb-2 pt-5 text-gray-600">
                &nbsp;{contentInfo.noticeDate}
              </td>
            </tr>
          </table>
          <br />
          <br />

          <table className="w-full border border-gray-400 border-x-0">
            <tr>
              <td className="pl-6 px-0 pt-24 pb-24 text-lg w-full whitespace-pre-line leading-10">
                {contentInfo.content?.replace(/\\n/g, "\n")}
              </td>
            </tr>
          </table>
        </div>
      ) : (
        <p className="text-center text-gray-500">데이터를 불러오는 중...</p>
      )}
      <br />
      <Link
        to="/main/noticelist"
        className="text-blue-500 hover:text-blue-700 bg-white text-lg font-semibold py-2.5 px-3 rounded transition"
        state={{ page: currentPage }}
      >
        ← 목록
      </Link>
      {/* 메시지 모달 */}
      <AlertModal
        isOpen={alertModalOpen}
        message={msg}
        onClose={handleClose}
        type={type}
      />
    </div>
  );
};

export default NoticeDataPage;
