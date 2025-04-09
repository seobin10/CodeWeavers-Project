import React, { useState, useEffect, useCallback } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import QnaAnswerWritePage from "../pages/Admin/QnaAnswerWritePage";
import AlertModal from "../components/AlertModal";
import BaseModal from "../components/BaseModal";
import ConfirmModal from "../components/ConfirmModal";
import { useSelector } from "react-redux";
import { getAuthHeader } from "../util/authHeader";

const QnaDataPage = () => {
  // 모달
  const [alertModalOpen, setAlertModalOpen] = useState(false);
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [baseModalOpen, setBaseModalOpen] = useState(false);

  // 모달 설정용 useState
  const [msg, setMsg] = useState("");
  const [type, setType] = useState("error");
  const [goTarget, setGoTarget] = useState(null);
  const [props, setProps] = useState();

  const [writerId, setWriterId] = useState();
  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState([]);
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const questionId = location.state?.questionId;
  const currentPage = location.state?.page;
  const userId = useSelector((state) => state.auth?.userId);
  const userRole = useSelector((state) => state.auth?.userRole);

  const handleClose = () => {
    setAlertModalOpen(false);
    setConfirmModalOpen(false);
    setBaseModalOpen(false);
    if (goTarget) {
      navigate(goTarget.path, goTarget.options || {});
      setGoTarget(null);
    } else {
      window.location.reload();
    }
  };

  const handleView = useCallback(async () => {
    try {
      await axios.put(
        `http://localhost:8080/api/user/qna/${questionId}/update`,
        null,
        getAuthHeader()
      );
    } catch (error) {
      console.log("조회수 증가 실패");
    }
  }, [questionId]);

  useEffect(() => {
    handleView();
    const localQnaNumber = localStorage.getItem("No.");

    if (questionId) {
      localStorage.setItem("No.", questionId);
      fetchContentInfo(questionId);
      fetchWriterId(questionId);
    } else if (localQnaNumber) {
      fetchContentInfo(localQnaNumber);
      fetchWriterId(localQnaNumber);
    }
  }, [questionId, userId, writerId, setWriterId, handleView]);

  const fetchContentInfo = async (id) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/qna/${id}`,
        getAuthHeader()
      );
      setContentInfo(response.data);
    } catch (error) {
      setMessage("게시물 정보를 불러올 수 없습니다.");
    }
  };

  const fetchWriterId = async (questionId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/qna/find/${questionId}`,
        getAuthHeader()
      );
      setWriterId(response.data);
      return response.data;
    } catch (error) {
      setMessage("작성자 정보를 불러올 수 없습니다.");
    }
  };

  const handleDelete = async (questionId) => {
    try {
      await axios.delete(
        `http://localhost:8080/api/user/qna/delete/${questionId}`,
        getAuthHeader()
      );
      setMsg("삭제되었습니다.");
      setType("success");
    } catch (error) {
      setMsg("삭제되지 않았습니다.");
      setType("error");
    } finally {
      setAlertModalOpen(true);
      setGoTarget({
        path: "/main/qnalist",
        options: {
          state: { page: currentPage },
        },
      });
    }
  };

  const handleClickDelete = async (questionId) => {
    try {
      await axios.delete(
        `http://localhost:8080/api/admin/ans/delete/${questionId}`,
        getAuthHeader()
      );
      setMsg("답변이 삭제되었습니다.");
      setType("success");
      setAlertModalOpen(true);
    } catch (error) {
      setMsg("답변이 삭제되지 않았습니다.");
      setType("error");
      setAlertModalOpen(true);
    }
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      {message && <p className="text-red-500 text-center">{message}</p>}
      <h1 className=" font-bold text-left mb-6 ml-6">Q&A</h1>
      {contentInfo.length > 0 ? (
        contentInfo.map((qna, i) => (
          <div key={i}>
            <table className="table-auto w-full">
              <thead className="bg-gray-200">
                <tr>
                  <td className="px-4 py-2 bg-white">
                    <div className="flex justify-between items-center relative">
                      <span className="text-blue-800 text-5xl font-semibold">
                        {qna.title}
                      </span>

                      <div className="relative">
                        <button
                          onClick={() => setMenuOpen(!menuOpen)}
                          className="text-gray-700 hover:text-gray-950  text-xl px-2"
                        >
                          ⋮
                        </button>

                        {menuOpen && (
                          <div className="absolute right-0 mt-2 w-24 bg-white border rounded shadow z-10 text-sm ">
                            <Link
                              onClick={
                                userId == writerId || userRole == "ADMIN"
                                  ? async () => {
                                      await handleDelete(questionId);
                                      setMsg("삭제되었습니다.");
                                      setType("success");
                                      setGoTarget({
                                        path: "/main/qnalist",
                                        options: {
                                          state: { page: currentPage },
                                        },
                                      });
                                      setAlertModalOpen(true);
                                    }
                                  : () => {
                                      setMsg("작성자만 삭제할 수 있습니다!");
                                      setType("error");
                                      setGoTarget(null);
                                      setAlertModalOpen(true);
                                    }
                              }
                              state={{ questionId }}
                              className="block px-4 py-2 hover:bg-gray-100 text-gray-800 border border-solid"
                            >
                              삭제
                            </Link>
                            {qna.answerContent ? (
                              <button
                                onClick={() => {
                                  setMsg("답변 완료 게시글은 수정 불가입니다.");
                                  setType("error");
                                  setGoTarget(null);
                                  setAlertModalOpen(true);
                                }}
                                className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-gray-800 border border-solid border-t-0"
                              >
                                수정불가
                              </button>
                            ) : (
                              <Link
                                to={userId == writerId ? "/main/qnaedit" : ""}
                                onClick={
                                  userId == writerId
                                    ? undefined
                                    : () => {
                                        setMsg("작성자만 수정할 수 있습니다!");
                                        setType("error");
                                        setGoTarget(null);
                                        setAlertModalOpen(true);
                                      }
                                }
                                state={{ questionId }}
                                className="block px-4 py-2 hover:bg-gray-100 text-gray-800 border border-solid border-t-0"
                              >
                                수정
                              </Link>
                            )}
                          </div>
                        )}
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="px-4 pb-2 pt-5 text-gray-600 bg-white">
                    {qna.userName} | {qna.createdAt} | {qna.viewCount}
                  </td>
                </tr>
              </thead>
            </table>
            <br />
            <table className="w-full border border-gray-400 border-x-0">
              <tbody>
                <tr>
                  <td className="pl-6 px-0 pt-24 pb-24 text-lg w-full whitespace-pre-line leading-10">
                    {qna.questionContent}
                  </td>
                </tr>
              </tbody>
            </table>
            <hr />
            <br />
            <table className="table-auto rounded-md w-full text-left">
              <tfoot>
                <tr>
                  <th className="bg-blue-800 text-white px-4 py-2 text-xl rounded-md">
                    답변내용
                  </th>
                </tr>
                <tr>
                  <td
                    className={`p-5 rounded-md shadow-md ${
                      !qna.answerContent ? "text-gray-400" : ""
                    }`}
                  >
                    {qna.answerContent || "아직 답변이 작성되지 않았습니다."}
                  </td>
                </tr>
              </tfoot>
            </table>
          </div>
        ))
      ) : (
        <p className="text-center text-gray-500"> 데이터를 불러오는 중...</p>
      )}

      <br />
      <Link
        to="/main/qnalist"
        className="text-blue-500 hover:text-blue-700 bg-white text-lg font-semibold py-2.5 px-3 rounded transition"
        state={{ page: currentPage }}
        onClick={() => {
          localStorage.removeItem("No.");
        }}
      >
        ← 목록
      </Link>

      {/* 관리자 전용 버튼 */}
      <div className="float-right">
        {userRole === "ADMIN" && contentInfo.length > 0 && (
          <>
            <button
              onClick={() => {
                const qna = contentInfo[0];
                if (!qna.answerContent || qna.answerContent.trim() === "") {
                  setProps(
                    <QnaAnswerWritePage qno={questionId} page={currentPage} />
                  );
                  setBaseModalOpen(true);
                } else {
                  setMsg("이미 답변이 작성되었습니다.");
                  setType("error");
                  setGoTarget(null);
                  setAlertModalOpen(true);
                }
              }}
              className="text-green-500 hover:text-green-700 text-lg font-semibold px-3 rounded transition"
            >
              📗답변 작성
            </button>
            &nbsp;
            <button
              onClick={() => {
                const qna = contentInfo[0];
                if (qna.answerContent) {
                  setConfirmModalOpen(true);
                } else {
                  setMsg("삭제할 답변이 없습니다.");
                  setType("error");
                  setGoTarget(null);
                  setAlertModalOpen(true);
                }
              }}
              className="text-red-500 hover:text-red-700 text-lg font-semibold py-2.5 px-3 rounded transition"
            >
              ❌답변 삭제
            </button>
          </>
        )}
      </div>

      {/* 모달 */}
      <AlertModal
        isOpen={alertModalOpen}
        message={msg}
        onClose={handleClose}
        type={type}
      />
      <BaseModal
        isOpen={baseModalOpen}
        onClose={handleClose}
        children={props}
      />
      <ConfirmModal
        isOpen={confirmModalOpen}
        message={"답변을 삭제하시겠습니까?"}
        onCancel={handleClose}
        onConfirm={() => {
          setConfirmModalOpen(false);
          handleClickDelete(questionId);
        }}
      />
    </div>
  );
};

export default QnaDataPage;
