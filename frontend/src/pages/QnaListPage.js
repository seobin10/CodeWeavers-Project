import React, { useContext, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../App";
import axios from "axios";
import PageComponent from "../components/PageComponent";

const QnaListPage = () => {
  const { userId, setUserId } = useContext(AuthContext);
  const [message, setMessage] = useState("");
  const [qnaInfo, setQnaInfo] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemCount = 15;

  const localId = localStorage.getItem("id");

  let frontNum = 0;

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
      const response = await axios.get(
        "http://localhost:8080/api/user/qna/list"
      );
      setQnaInfo(response.data);
    } catch (error) {
      setMessage("Q&A 정보를 불러올 수 없습니다.");
    }
  };

  const handlePage = (page) => {
    setCurrentPage(page);
  };

  const lastItem = currentPage * itemCount;
  const firstItem = lastItem - itemCount;
  const currentItem = qnaInfo.slice(firstItem, lastItem);
  const totalPage = Math.ceil(qnaInfo.length / itemCount);

  return (
    <div>
      <h1 className="text-3xl font-bold text-center mb-6">Q&A</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      <div>
        <table className="table-auto border-collapse border border-gray-400 w-full">
          <thead className="bg-gray-200">
            <tr>
              <th className="border border-gray-400 px-4 py-2">번호</th>
              <th className="border border-gray-400 px-4 py-2">제목</th>
              <th className="border border-gray-400 px-4 py-2">작성자</th>
              <th className="border border-gray-400 px-4 py-2">작성일</th>
              <th className="border border-gray-400 px-4 py-2">조회수</th>
              <th className="border border-gray-400 px-4 py-2">상태</th>
            </tr>
          </thead>
          <tbody>
            {currentItem.length > 0 ? (
              currentItem.map((qna, i) => (
                <tr key={i} className="text-center">
                  <td className="border border-gray-400 px-4 py-2">
                    {firstItem + i + 1}
                  </td>
                  <td className="text-left border border-gray-400 px-4 py-2">
                    <Link
                      to="/main/qnadata"
                      state={{ questionId: qna.questionId }}
                    >
                      {qna.title}
                    </Link>
                  </td>
                  <td className="border border-gray-400 px-4 py-2">
                    {qna.userName}
                  </td>
                  <td className="border border-gray-400 px-4 py-2">
                    {qna.createdAt}
                  </td>
                  <td className="border border-gray-400 px-4 py-2">
                    {qna.viewCount}
                  </td>
                  <td className="border border-gray-400 px-4 py-2">
                    {qna.status}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="text-center text-gray-500 py-4">
                  Q&A 정보를 불러오는 중...
                </td>
              </tr>
            )}
          </tbody>
        </table>
        <PageComponent
          currentPage={currentPage}
          totalPage={totalPage}
          onPageChange={handlePage}
        />
      </div>
      <br />
      <Link
        to="/main/qnawrite"
        className=" bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition float-right"
      >
        &nbsp;등록&nbsp;
      </Link>
    </div>
  );
};

export default QnaListPage;
