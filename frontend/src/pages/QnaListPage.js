import React, { useContext, useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../App";
import axios from "axios";
import PageComponent from "../components/PageComponent";

const QnaListPage = () => {
  const navigate = useNavigate();
  const { userId, setUserId } = useContext(AuthContext);
  const [message, setMessage] = useState("");
  const [qnaInfo, setQnaInfo] = useState([]);
  const [writerId, setWriterId] = useState();
  const [currentPage, setCurrentPage] = useState(1);
  const itemCount = 15;

  const localId = localStorage.getItem("id");
  const userRole = localStorage.getItem("role");

  useEffect(() => {
    if (userId) {
      fetchQnaInfo(userId);
    } else if (localId) {
      setUserId(localId);
      fetchQnaInfo(localId);
    }
  }, [userId, setUserId, localId]);

  const fetchQnaInfo = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/user/qna/list"
      );
      setQnaInfo(response.data);
    } catch (error) {
      setMessage("Q&A 정보를 불러올 수 없습니다.");
    }
  };

  const fetchWriterId = async (questionId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/qna/find/${questionId}`
      );
      setWriterId(response.data);
      return response.data;
    } catch (error) {
      setMessage("작성자 정보를 불러올 수 없습니다.");
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
                    {/\u{1F512}/u.test(qna.title) ? (
                      <p
                        className="text-gray-400"
                        onClick={async () => {
                          let writerId = await fetchWriterId(qna.questionId);
                          let message = "";
                          // 두 아이디의 타입이 다르므로 !== 대신 != 사용
                          if (userId == writerId || userRole === "ADMIN") {
                            message =
                              userRole === "ADMIN"
                                ? "관리자 권한 확인, 글을 조회합니다."
                                : "본인 확인 완료! 글을 조회합니다.";
                            alert(message);
                            navigate("/main/qnadata", {
                              state: { questionId: qna.questionId },
                            });
                          } else {
                            message = "읽을 수 있는 권한이 없습니다.";
                            alert(message);
                          }
                        }}
                      >
                        &#128274; 비밀글입니다.
                      </p>
                    ) : (
                      <Link
                        to="/main/qnadata"
                        state={{ questionId: qna.questionId }}
                      >
                        {qna.title}
                      </Link>
                    )}
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
      {userRole === "ADMIN" ? (
        <></>
      ) : (
        <Link
          to="/main/qnawrite"
          className="bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition float-right"
        >
          &nbsp;등록&nbsp;
        </Link>
      )}
    </div>
  );
};

export default QnaListPage;
