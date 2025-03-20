import React, { useContext, useState, useEffect, useCallback } from "react";
import { Link, useLocation } from "react-router-dom";
import { AuthContext } from "../App";
import axios from "axios";

const QnaDataPage = () => {
  const location = useLocation();
  const questionId = location.state?.questionId;

  const { userId, setUserId } = useContext(AuthContext);
  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState([]);

  const handleView = useCallback(
    async () => {
      try{
        await axios.put(
        `http://localhost:8080/api/user/qna/${questionId}/update`,
        );
      } catch (error) {
        console.log("조회수 증가 실패");
      }
},[questionId]);

    useEffect(() => {
      handleView();
      const localQnaNumber = localStorage.getItem("No.");

      if (questionId) {
        localStorage.setItem("No.", questionId);
        fetchContentInfo(questionId);
      } else if (localQnaNumber) {
        setUserId(localQnaNumber);
        fetchContentInfo(localQnaNumber);
      }
      
    }, [questionId, userId, setUserId, handleView]);

    const fetchContentInfo = async (id) => {
      try {
        const response = await axios.get(`http://localhost:8080/api/user/qna/${id}`);

        setContentInfo(response.data);
      } catch (error) {
        setMessage("게시물 정보를 불러올 수 없습니다.");
      }
    };


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
        <Link
          to="/main/qnalist"
          button
          className="bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-1 px-3 rounded transition"
        >
          목록
        </Link>
      </div>
    );
  };

export default QnaDataPage;
