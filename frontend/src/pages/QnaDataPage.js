import React, { useState, useEffect, useCallback } from "react";
import { Link, useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import QnaAnswerModal from "../components/QnaAnswerModal";
import QnaAnswerWritePage from "../pages/Admin/QnaAnswerWritePage";
import QnaAnswerDeletePage from "./Admin/QnaAnswerDeletePage";
import { WaitModalClick } from "../components/WaitModalClick";
import { showModal } from "../slices/modalSlice"; // 예시

const QnaDataPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [props, setProps] = useState();
  const isOpen = isModalOpen;
  const handleClose = () => setIsModalOpen(false);

  const dispatch = useDispatch();
  const location = useLocation();
  const questionId = location.state?.questionId;
  const currentPage = location.state?.page;

  const userId = useSelector((state) => state.login.userId);
  const userRole = useSelector((state) => state.login.userRole);

  const [writerId, setWriterId] = useState();
  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState([]);
  const [menuOpen, setMenuOpen] = useState(false);

  const refetchData = () => {
    if (questionId) {
      fetchContentInfo(questionId);
      fetchWriterId(questionId);
    }
  };

  const handleView = useCallback(async () => {
    try {
      await axios.put(
        `http://localhost:8080/api/user/qna/${questionId}/update`
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
  }, [questionId, handleView]);

  const fetchContentInfo = async (id) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/qna/${id}`
      );
      setContentInfo(response.data);
    } catch (error) {
      setMessage("게시물 정보를 불러올 수 없습니다.");
    }
  };

  const fetchWriterId = async (questionId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/qna/find/${questionId}`
      );
      setWriterId(response.data);
    } catch (error) {
      setMessage("작성자 정보를 불러올 수 없습니다.");
    }
  };

  const openModalWithCheck = (text) => dispatch(showModal(text));

  return (
    <div>
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 조회</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      {contentInfo.length > 0 ? (
        contentInfo.map((qna, i) => (
          <div key={i}>
            <table className="table-auto border-collapse border border-gray-400 w-full">
              <thead className="bg-gray-200">
                <tr>
                  <th className="border border-gray-400 px-4 py-2">제목</th>
                  <td className="border border-gray-400 px-4 py-2 bg-white">
                    <div className="flex justify-between items-center relative">
                      <span>{qna.title}</span>
                      <div className="relative">
                        <button
                          onClick={() => setMenuOpen(!menuOpen)}
                          className="text-gray-700 hover:text-gray-950  text-xl px-2"
                        >
                          ⋮
                        </button>

                        {menuOpen && (
                          <div className="absolute right-0 mt-2 w-24 bg-white border rounded shadow z-10 text-sm">
                            <Link
                              to={
                                userId == writerId || userRole === "ADMIN"
                                  ? "/main/qnadelete"
                                  : ""
                              }
                              onClick={
                                userId == writerId || userRole === "ADMIN"
                                  ? undefined
                                  : () =>
                                      openModalWithCheck(
                                        "작성자만 삭제할 수 있습니다!"
                                      )
                              }
                              state={{ questionId }}
                              className="block px-4 py-2 hover:bg-gray-100 text-gray-800 border border-solid border-black-300"
                            >
                              삭제
                            </Link>
                            {qna.answerContent ? (
                              <button
                                onClick={() =>
                                  openModalWithCheck(
                                    "답변 완료 게시글은 수정 불가입니다."
                                  )
                                }
                                className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-gray-800 border border-solid border-black-300 border-t-0"
                              >
                                수정불가
                              </button>
                            ) : (
                              <Link
                                to={userId == writerId ? "/main/qnaedit" : ""}
                                onClick={
                                  userId == writerId
                                    ? undefined
                                    : () =>
                                        openModalWithCheck(
                                          "작성자만 수정할 수 있습니다!"
                                        )
                                }
                                state={{ questionId }}
                                className="block px-4 py-2 hover:bg-gray-100 text-gray-800 border border-solid border-black-300 border-t-0"
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
                  <th className="border border-gray-400 px-4 py-2">작성자</th>
                  <td className="border border-gray-400 px-4 py-2 bg-white">
                    {qna.userName}
                  </td>
                </tr>
                <tr>
                  <th className="border border-gray-400 px-4 py-2">
                    등록일 / 조회수
                  </th>
                  <td className="border border-gray-400 px-4 py-2 bg-white">
                    {qna.createdAt} / {qna.viewCount}
                  </td>
                </tr>
              </thead>
            </table>

            <table>
              <tbody>
                <tr>
                  <td className="p-5 text-left">{qna.questionContent}</td>
                </tr>
              </tbody>
            </table>
            <hr />
            <br />
            <table className="table-auto border-collapse border w-full text-left">
              <tfoot>
                <tr>
                  <th className="bg-gray-200 px-4 py-2">답변내용</th>
                </tr>
                <tr>
                  <td
                    className={`p-5 ${
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
        <p className="text-center text-gray-500">데이터를 불러오는 중...</p>
      )}
      <br />
      <Link
        to="/main/qnalist"
        className=" bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
        state={{ page: currentPage }}
      >
        목록
      </Link>

      {userRole === "ADMIN" && contentInfo.length > 0 && (
        <div className="float-right">
          <button
            onClick={() => {
              const qna = contentInfo[0];
              if (!qna.answerContent) {
                setProps(
                  <QnaAnswerWritePage qno={questionId} page={currentPage} />
                );
                setIsModalOpen(true);
              } else {
                openModalWithCheck("이미 답변이 작성되었습니다.");
              }
            }}
            className="bg-green-500 hover:bg-green-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
          >
            답변 작성
          </button>
          &nbsp;
          <button
            onClick={() => {
              const qna = contentInfo[0];
              if (qna.answerContent) {
                setProps(
                  <QnaAnswerDeletePage qno={questionId} page={currentPage} />
                );
                setIsModalOpen(true);
              } else {
                openModalWithCheck("삭제할 답변이 없습니다.");
              }
            }}
            className="bg-red-500 hover:bg-red-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
          >
            답변 삭제
          </button>
        </div>
      )}

      <QnaAnswerModal
        isOpen={isOpen}
        onClose={handleClose}
        onSuccess={refetchData}
        pageProps={props}
      />
    </div>
  );
};

export default QnaDataPage;
