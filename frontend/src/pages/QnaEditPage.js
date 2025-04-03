import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
import { WaitModalClick } from "../components/WaitModalClick";
import { useModal } from "../hooks/useModal"; // 커스텀 훅 가정

const QnaEditPage = () => {
  const location = useLocation();
  const questionId = location.state?.questionId;
  const navigate = useNavigate();

  // Redux 상태로부터 사용자 정보
  const { userId, userRole } = useSelector((state) => state.login);
  const { showModal } = useModal(); // Redux dispatch or 커스텀 Modal 훅

  const [writerId, setWriterId] = useState();
  const [message, setMessage] = useState("");
  const [contentInfo, setContentInfo] = useState([]);
  const [contentData, setContentData] = useState({
    questionId: 0,
    title: "",
    content: "",
  });

  // QnA 데이터 조회
  useEffect(() => {
    if (questionId) {
      localStorage.setItem("No.", questionId);
      fetchContentInfo(questionId);
      fetchWriterId(questionId);
    }
  }, [questionId]);

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
      const res = await axios.get(`http://localhost:8080/api/user/qna/${id}`);
      setContentInfo(res.data);
    } catch (err) {
      setMessage("게시물 정보를 불러올 수 없습니다.");
    }
  };

  const fetchWriterId = async (qid) => {
    try {
      const res = await axios.get(
        `http://localhost:8080/api/user/qna/find/${qid}`
      );
      setWriterId(res.data);
    } catch (err) {
      setMessage("작성자 정보를 불러올 수 없습니다.");
    }
  };

  const cleanedTitle = (title) => {
    return /\u{1F512}/u.test(title)
      ? title.replace(/\u{1F512}\s*/gu, "")
      : title;
  };

  const handleSecret = (title) => {
    const isSecret = document.getElementById("secret").checked;
    const cleanTitle = cleanedTitle(title);
    return isSecret ? `🔒 ${cleanTitle}` : cleanTitle;
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
      if (!contentData.title.trim() || !contentData.content.trim())
        return false;

      const updatedTitle = handleSecret(contentData.title);

      await axios.put(`http://localhost:8080/api/user/qna/edit/${questionId}`, {
        ...contentData,
        title: updatedTitle,
      });
      return true;
    } catch (err) {
      return false;
    }
  };

  const editQna = async () => {
    if (userId == writerId) {
      const success = await handleEdit();
      showModal(
        success
          ? "성공적으로 수정되었습니다."
          : "제목 혹은 내용을 입력해주세요."
      );
      await WaitModalClick();
      if (success) {
        navigate("/main/qnadata", { state: { questionId } });
        setTimeout(() => window.location.reload(), 0);
      }
    } else {
      showModal("작성자만 수정할 수 있습니다!");
      await WaitModalClick();
      navigate("/main/qnalist");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 수정</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <div className="bg-green-200 rounded-lg p-4 mb-4">
        <span>수정한 내용을 저장하시겠습니까?</span>
        <button
          onClick={editQna}
          className="ml-4 bg-green-600 text-white px-4 py-1 rounded"
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
                      name="title"
                      className="w-full focus-visible:outline-none"
                      value={cleanedTitle(contentData.title)}
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
            <p>
              {/\u{1F512}/u.test(qna.title) ? (
                <>
                  <input type="checkbox" id="secret" defaultChecked={true} />{" "}
                  비밀 글
                </>
              ) : (
                <>
                  <input type="checkbox" id="secret" /> 비밀 글
                </>
              )}
            </p>
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
