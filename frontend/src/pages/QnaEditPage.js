import React, { useContext, useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { AuthContext } from "../App";
import axios from "axios";

const QnaEditPage = () => {
  const location = useLocation();
  const questionId = location.state?.questionId;
  const [writerId, setWriterId] = useState();
  const { userId, setUserId } = useContext(AuthContext);
  const [userInfo, setUserInfo] = useState(null);
  const [userData, setUserData] = useState({
    userName: "",
    userId: "",
    userBirth: "",
    userEmail: "",
    userPhone: "",
    userImgUrl: "",
    departmentName: "",
  });

  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState([]);
  const [contentData, setContentData] = useState({
    questionId: 0,
    title: "",
    content: "",
  });
  const navigate = useNavigate();

  useEffect(() => {
    const localQnaNumber = localStorage.getItem("No.");

    if (questionId) {
      localStorage.setItem("No.", questionId);
      fetchContentInfo(questionId);
      fetchWriterId(questionId);
    } else if (localQnaNumber) {
      setUserId(localQnaNumber);
      fetchContentInfo(localQnaNumber);
    }
  }, [questionId, userId, setUserId]);

  useEffect(() => {
    if (contentInfo.length > 0) {
      const qna = contentInfo[0];
      setContentData({
        questionId: qna.questionId,
        title: qna.title,
        content: qna.questionContent,
      });
    }
  }, [contentInfo]);

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

  const fetchUserInfo = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/${userId}`
      );
      setUserInfo(response.data);
      setUserData({
        userName: response.data.userName,
        userId: response.data.userId,
        userBirth: response.data.userBirth,
        userEmail: response.data.userEmail,
        userPhone: response.data.userPhone,
        userImgUrl: response.data.userImgUrl,
        departmentName: response.data.departmentName || "",
      });
      return response.data;
    } catch (error) {
      setMessage("유저 정보를 불러올 수 없습니다.");
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

  const updateQnaData = async (contentData) => {
    try {
      if (!contentData?.questionId) throw new Error("Invalid questionId!");
      const res = await axios.put(
        `http://localhost:8080/api/user/qna/edit/${questionId}`,
        contentData
      );
      return res.data;
    } catch (error) {
      console.error("게시글 정보 업데이트 실패:", error);
      return { error: "게시글 업데이트 실패. 다시 시도해주세요." };
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setContentData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleEdit = async () => {
    try {
      await updateQnaData({
        questionId: questionId,
        title: contentData.title,
        content: contentData.content,
      });
      setMessage("정보가 업데이트되었습니다.");
    } catch (error) {
      setMessage("정보 수정에 실패했습니다.");
    }
  };

  const editQna = async () => {
    let currentId = userData.userId;
    if (!currentId) {
      const fetched = await fetchUserInfo(userId);
      currentId = fetched?.userId;
    }
    // currentId와 writerId의 타입이 다르므로, === 대신 == 사용
    if (currentId == writerId) {
      alert("수정되었습니다.");
      await handleEdit();
        navigate("/main/qnadata", { state: { questionId } });
        window.location.reload();
        
    } else {
      alert("작성자만 수정할 수 있습니다!");
      //   navigate("/main/qnalist");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 수정</h1>
      {message && <p className="text-green-500 text-center">{message}</p>}
      <hr />
      <br />
      <div className="bg-green-200 rounded-lg pt-4 pb-4 text-green-600 border border-green-300 flex justify-between items-center px-4">
        <span>수정한 내용을 저장하시겠습니까?</span>
        <button
          className="bg-green-600 hover:bg-green-800 text-white text-sm font-semibold rounded transition px-3 py-1
        "
          onClick={editQna}
        >
          수정
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
                    <input
                      placeholder={qna.title}
                      name="title"
                      className="w-full focus-visible:outline-none"
                      value={contentData.title}
                      onChange={handleChange}
                    />
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
            <table className="table-auto w-full border-collapse border border-gray-400">
              <tbody>
                <tr className="h-96">
                  <td colSpan={2} className="p-4 w-full">
                    <input
                      type="text"
                      placeholder={qna.questionContent}
                      name="content"
                      className="w-full h-96 focus-visible:outline-none text-left px-4 py-2"
                      maxLength={255}
                      value={contentData.content}
                      onChange={handleChange}
                    />
                  </td>
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

export default QnaEditPage;
