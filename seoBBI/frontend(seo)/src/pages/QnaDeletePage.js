import React, { useEffect, useState, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
import { WaitModalClick } from "../components/WaitModalClick";
import { useModal } from "../hooks/useModal"; // 커스텀 훅 또는 context 대체

const QnaDeletePage = () => {
  const location = useLocation();
  const questionId = location.state?.questionId;
  const [writerId, setWriterId] = useState();
  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState([]);
  const navigate = useNavigate();

  // Redux에서 유저 정보 가져오기
  const {
    userId,
    userName,
    userBirth,
    userEmail,
    userPhone,
    userImgUrl,
    departmentName,
    userRole,
  } = useSelector((state) => state.login);

  const { showModal } = useModal(); // ModalContext 대체 커스텀 훅

  useEffect(() => {
    const localQnaNumber = localStorage.getItem("No.");

    if (questionId) {
      localStorage.setItem("No.", questionId);
      fetchContentInfo(questionId);
      fetchWriterId(questionId);
    } else if (localQnaNumber) {
      fetchContentInfo(localQnaNumber);
    }
  }, [questionId]);

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

  const handleDelete = useCallback(async () => {
    try {
      await axios.delete(
        `http://localhost:8080/api/user/qna/delete/${questionId}`
      );
    } catch (error) {
      console.error("❌ 데이터 삭제 실패:", error);
    }
  }, [questionId]);

  const deleteQna = async () => {
    let msg = "";
    if (userId == writerId || userRole === "ADMIN") {
      msg =
        userRole === "ADMIN" && userId != writerId
          ? "관리자 권한 확인, 삭제되었습니다"
          : "본인 확인 완료! 삭제되었습니다";
      await handleDelete();
      showModal(msg);
      await WaitModalClick();
      navigate("/main/qnalist");
    } else {
      showModal("삭제할 수 있는 권한이 없습니다.");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 삭제</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      <div className="bg-red-200 rounded-lg pt-4 pb-4 text-red-600 border border-red-300 flex justify-between items-center px-4">
        <span>정말로 삭제하시겠습니까?</span>
        <button
          className="bg-red-600 hover:bg-red-800 text-white text-sm font-semibold rounded transition px-3 py-1"
          onClick={deleteQna}
        >
          삭제
        </button>
      </div>
      <br />
      {contentInfo.length > 0 ? (
        contentInfo.map((qna, i) => (
          <div key={i}>
            <table className="table-auto border-collapse border border-gray-400 w-full">
              <thead className="bg-gray-200">
                <tr>
                  <th className="border border-gray-400 px-4 py-2">제목</th>
                  <td className="border border-gray-400 px-4 py-2 bg-white">
                    {qna.title}
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
        <p className="text-center text-gray-500"> 데이터를 불러오는 중...</p>
      )}
      <br />
    </div>
  );
};

export default QnaDeletePage;
