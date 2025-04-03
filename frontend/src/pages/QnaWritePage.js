import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { WaitModalClick } from "../components/WaitModalClick";
import { useModal } from "../hooks/useModal"; // ✅ 커스텀 훅 사용

const today = new Date().toISOString().split("T")[0];

const QnaWritePage = () => {
  const navigate = useNavigate();
  const { showModal } = useModal(); // ✅ 커스텀 훅 사용

  const user = useSelector((state) => state.login || {});
  const [message, setMessage] = useState("");
  const [formData, setFormData] = useState({
    questionId: null,
    title: "",
    content: "",
    status: "OPEN",
    viewCount: 0,
  });

  const handleSecret = (title) => {
    const isSecret = document.getElementById("secret").checked;
    return isSecret
      ? `🔒 ${title.replace(/^🔒\s*/, "")}`
      : title.replace(/^🔒\s*/, "");
  };

  const postAdd = async (textObj) => {
    const headers = { "Content-Type": "application/json" };
    const res = await axios.post(
      `http://localhost:8080/api/user/qna/write?userId=${user.userId}`,
      textObj,
      { headers }
    );
    return res.data;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleClickAdd = async () => {
    if (!formData.title.trim() || !formData.content.trim()) {
      showModal("제목 혹은 내용을 입력해주세요");
      return;
    }

    const obj = {
      ...formData,
      title: handleSecret(formData.title),
      userName: user.userName,
      userId: user.userId,
      createdAt: today,
    };

    await postAdd(obj);
    showModal("질문이 등록되었습니다.");
    await WaitModalClick();
    navigate("/main/qnalist");
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 등록</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      <table className="table-auto border-collapse border border-gray-400 w-full">
        <thead className="bg-gray-200">
          <tr>
            <th className="border border-gray-400 px-4 py-2">제목</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                placeholder="제목을 작성하세요"
                name="title"
                className="w-full focus-visible:outline-none"
                onChange={handleChange}
                value={formData.title}
              />
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2">작성자</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                name="userName"
                readOnly
                className="w-full focus-visible:outline-none"
                value={user.userName || ""}
              />
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2">작성일</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                value={today}
                readOnly
                className="w-full focus-visible:outline-none"
              />
            </td>
          </tr>
        </thead>
        <tbody>
          <tr className="w-full h-96 flex-auto">
            <td colSpan={2} className="p-4">
              <input
                placeholder="질문 내용을 작성하세요."
                name="content"
                className="w-full h-96 focus-visible:outline-none"
                maxLength={255}
                onChange={handleChange}
                value={formData.content}
              ></input>
            </td>
          </tr>
        </tbody>
      </table>
      <div className="mt-4">
        <p title="비밀 글을 작성하고 싶다면 체크하세요">
          <input type="checkbox" id="secret" /> 비밀 글
        </p>
        <div className="flex float-right">
          <Link
            to="/main/qnalist"
            className=" bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
          >
            돌아가기
          </Link>
          &nbsp;
          <button
            className=" bg-green-500 hover:bg-green-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
            onClick={handleClickAdd}
          >
            작성하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default QnaWritePage;
