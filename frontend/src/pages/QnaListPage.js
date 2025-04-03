import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
import PageComponent from "../components/PageComponent";
import { WaitModalClick } from "../components/WaitModalClick";
import { useModal } from "../hooks/useModal";

const QnaListPage = () => {
  const navigate = useNavigate();
  const { showModal } = useModal();
  const [message, setMessage] = useState("");
  const [qnaInfo, setQnaInfo] = useState([]);
  const location = useLocation();
  const checkPage = location.state?.page ?? 1;
  const [currentPage, setCurrentPage] = useState(checkPage);
  const itemCount = 15;

  const user = useSelector((state) => state.login || {});
  const { userId, userRole } = user;

  useEffect(() => {
    if (userId) {
      fetchQnaInfo();
    }
  }, [userId]);

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
                        className="text-gray-400 cursor-pointer"
                        onClick={async () => {
                          let writerId = await fetchWriterId(qna.questionId);
                          if (userId == writerId) {
                            showModal("본인 확인 완료! 글을 조회합니다.");
                            await WaitModalClick();
                            navigate("/main/qnadata", {
                              state: {
                                questionId: qna.questionId,
                                page: currentPage,
                              },
                            });
                          } else if (
                            ["ADMIN", "PROFESSOR"].includes(userRole)
                          ) {
                            showModal("권한 확인 완료! 글을 조회합니다.");
                            await WaitModalClick();
                            navigate("/main/qnadata", {
                              state: {
                                questionId: qna.questionId,
                                page: currentPage,
                              },
                            });
                          } else {
                            showModal("읽을 수 있는 권한이 없습니다.");
                          }
                        }}
                      >
                        🔒 비밀글입니다.
                      </p>
                    ) : (
                      <Link
                        to="/main/qnadata"
                        state={{
                          questionId: qna.questionId,
                          page: currentPage,
                        }}
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
      {userRole !== "ADMIN" && (
        <Link
          to="/main/qnawrite"
          className="bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition float-right"
        >
          등록
        </Link>
      )}
    </div>
  );
};

export default QnaListPage;
