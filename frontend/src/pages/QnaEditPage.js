import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { getQnaDetail, getQnaWriterId, updateQna } from "../api/qnaApi";
import AlertModal from "../components/AlertModal";

const QnaEditPage = () => {
  const location = useLocation();
  const questionId = location.state?.questionId;
  const navigate = useNavigate();
  const userId = useSelector((state) => state.auth?.userId);

  const [writerId, setWriterId] = useState(null);
  const [message, setMessage] = useState("");
  const [contentData, setContentData] = useState({
    title: "",
    content: "",
    questionId,
  });

  const [alertModalOpen, setAlertModalOpen] = useState(false);
  const [msg, setMsg] = useState("");
  const [type, setType] = useState("");
  const [goTarget, setGoTarget] = useState(null);

  useEffect(() => {
    if (questionId) {
      fetchContent();
      fetchWriter();
    }
  }, [questionId]);

  const fetchContent = async () => {
    try {
      const res = await getQnaDetail(questionId);
      const qna = res.data[0];
      setContentData({
        questionId: qna.questionId,
        title: qna.title,
        content: qna.questionContent,
        createdAt: qna.createdAt,
        viewCount: qna.viewCount,
        userName: qna.userName,
      });
    } catch (err) {
      setMessage("질문을 불러올 수 없습니다.");
    }
  };

  const fetchWriter = async () => {
    try {
      const res = await getQnaWriterId(questionId);
      setWriterId(res.data);
    } catch (err) {
      setMessage("작성자 정보를 불러올 수 없습니다.");
    }
  };

  const handleSecret = (title) => {
    const isSecret = document.getElementById("secret").checked;
    return isSecret ? `🔒 ${title.replace(/^🔒\s*/, "")}` : title.replace(/^🔒\s*/, "");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setContentData((prev) => ({ ...prev, [name]: value }));
  };

  const setAlertData = (modalType, modalMsg, targetPath = null) => {
    setType(modalType);
    setMsg(modalMsg);
    setGoTarget(targetPath);
    setAlertModalOpen(true);
  };

  const handleClose = () => {
    setAlertModalOpen(false);
    if (goTarget) {
      navigate(goTarget, { state: { questionId } });
      setGoTarget(null);
    }
  };

  const handleSubmit = async () => {
    if (!contentData.title.trim() || !contentData.content.trim()) {
      setAlertData("error", "제목과 내용을 모두 입력해주세요.");
      return;
    }

    if (userId != writerId) {
      console.log("u ", userId);
      console.log("w ", writerId);
      setAlertData("error", "작성자만 수정할 수 있습니다.", "/main/qnalist");
      return;
    }

    try {
      const updatedTitle = handleSecret(contentData.title);
      await updateQna(questionId, { ...contentData, title: updatedTitle });
      setAlertData("success", "성공적으로 수정되었습니다.", "/main/qnadata");
    } catch (err) {
      setAlertData("error", "수정에 실패했습니다.");
    }
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <h1 className="text-md font-bold text-left mb-6">Q&A 수정</h1>
      <hr />
      <br />
      {message && <p className="text-red-500 text-center">{message}</p>}
      <table className="table-auto border-collapse border border-gray-400 w-full">
        <thead className="bg-blue-800">
          <tr>
            <th className="border border-gray-400 px-4 py-2 text-white">제목</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                name="title"
                className="w-full focus-visible:outline-none"
                value={contentData.title.replace(/^🔒\s*/, "")}
                onChange={handleChange}
              />
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2 text-white">작성자</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              {"시온" || ""}
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2 text-white">작성일 / 조회수</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              {contentData.createdAt} / {contentData.viewCount}
            </td>
          </tr>
        </thead>
        <tbody>
          <tr className="w-full h-96 flex-auto shadow-md">
            <td colSpan={2} className="p-4">
              <textarea
                placeholder="질문 내용을 수정하세요."
                name="content"
                className="w-full h-96 focus-visible:outline-none resize-none"
                maxLength={255}
                onChange={handleChange}
                value={contentData.content}
              />
            </td>
          </tr>
        </tbody>
      </table>
      <div className="mt-4">
        <label>
          <input
            type="checkbox"
            id="secret"
            defaultChecked={/^🔒/.test(contentData.title)}
          />{" "}
          비밀 글
        </label>
        <div className="flex float-right mb-10">
          <button
            onClick={handleSubmit}
            className="text-green-500 hover:text-green-700 text-lg font-semibold px-3 rounded transition"
          >
            📗 수정하기
          </button>
        </div>
        <br />
      </div>
      <AlertModal
        isOpen={alertModalOpen}
        message={msg}
        type={type}
        onClose={handleClose}
      />
    </div>
  );
};

export default QnaEditPage;
