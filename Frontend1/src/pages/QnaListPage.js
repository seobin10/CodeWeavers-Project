import React, { useContext, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../App";
import axios from "axios";

const QnaListPage = () => {
  const { userId, setUserId } = useContext(AuthContext);
  const [message, setMessage] = useState("");
  const [qnaInfo, setQnaInfo] = useState([]);

  const localId = localStorage.getItem("id");


  
  useEffect(() => {
    if (userId) {
      fetchQnaInfo(userId);
    } else if (localId) {
      setUserId(localId);
      fetchQnaInfo(localId);
    }
  }, [userId, setUserId, localId]);

  const fetchQnaInfo = async (userId) => {
    try {
      const response = await axios.get("http://localhost:8080/api/user/qna/list");
      setQnaInfo(response.data);
    } catch (error) {
      setMessage("Q&A 정보를 불러올 수 없습니다.");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-center mb-6"> Q & A </h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      <div>
        <table className="table-auto border-collapse border border-gray-400 w-full">
          <thead className="bg-blue-800 bg-opacity-90 text-white">
            <tr>
              <th className="border border-gray-500 px-4 py-2">번호</th>
              <th className="border border-gray-500 px-4 py-2">제목</th>
              <th className="border border-gray-500 px-4 py-2">작성자</th>
              <th className="border border-gray-500 px-4 py-2">작성일</th>
              <th className="border border-gray-500 px-4 py-2">조회수</th>
              <th className="border border-gray-500 px-4 py-2">상태</th>
            </tr>
          </thead>

          <tbody>
            {qnaInfo.length > 0 ? (
              qnaInfo.map((qna, i) => (
                <tr key={i} className="text-center">
                  <td className="bg-gray-100 bg-opacity-90 border border-gray-400 px-4 py-2">{qna.questionId}</td>
                  <td className="text-left border border-gray-400 px-4 py-2">
                    <Link to="/main/qnadata" state={{questionId : qna.questionId}}>{qna.title}</Link>
                  </td>
                  <td className="border border-gray-400 px-4 py-2">{qna.userName}</td>
                  <td className="border border-gray-400 px-4 py-2">{qna.createdAt}</td>
                  <td className="border border-gray-400 px-4 py-2">{qna.viewCount}</td>
                  <td className="bg-gray-100 bg-opacity-90  border border-gray-400 px-4 py-2">{qna.status}</td>
                </tr>
              ))
            ) : (
              <p className="text-center text-gray-500">Q&A 정보를 불러오는 중...</p>
            )}
          </tbody>
        </table>
      </div><br/>
      <Link to = "/main/qnawrite" className="bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-1 px-3 rounded transition">등록</Link>
    </div>
    
  );
};

export default QnaListPage;
