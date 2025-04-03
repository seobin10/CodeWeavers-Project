import axios from "axios";
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { WaitModalClick } from "../../components/WaitModalClick";
import { showModal } from "../../slices/modalSlice";

// 날짜 데이터 포맷팅
const date = new Date();
const year = date.getFullYear();
const month = String(date.getMonth() + 1).padStart(2, "0");
const day = String(date.getDate()).padStart(2, "0");
const today = `${year}-${month}-${day}`;

const QnaAnswerWritePage = ({ qno }) => {
  const userId = useSelector((state) => state.auth.userId);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const currentPage = location.state?.page;

  const [formData, setFormData] = useState({
    answerId: null,
    questionId: null,
    userId: userId,
    answer: "",
    answerDate: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleClickAdd = async (e) => {
    if (!formData.answer.trim()) {
      dispatch(showModal("내용을 입력해주세요"));
      return e.preventDefault();
    }

    const obj = {
      answerId: null,
      questionId: parseInt(qno, 10),
      userId: userId,
      answer: formData.answer,
      answerDate: today,
    };

    try {
      await postAdd(obj);
      dispatch(showModal("답변이 등록되었습니다."));
      await WaitModalClick();
      navigate("/main/qnaData", {
        state: { questionId: qno, page: currentPage },
      });
      setTimeout(() => window.location.reload(), 0);
    } catch (error) {
      dispatch(showModal("네트워크 상태가 좋지 않습니다."));
      await WaitModalClick();
      navigate("/main/qnaData", {
        state: { questionId: qno, page: currentPage },
      });
      setTimeout(() => window.location.reload(), 0);
    }
  };

  const postAdd = async (textObj) => {
    const headers = { "Content-Type": "application/json" };
    const res = await axios.post(
      "http://localhost:8080/api/admin/ans/write",
      textObj,
      { headers }
    );
    return res.data;
  };

  return (
    <div className="border border-solid shadow-md p-10 rounded-md bg-white">
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 답변</h1>
      <hr />
      <br />
      <table className="table-auto border-collapse border border-gray-400 w-full">
        <thead className="bg-gray-200">
          <tr>
            <th className="border border-gray-400 px-4 py-2">작성자ID</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                name="userId"
                readOnly
                className="w-full focus-visible:outline-none"
                value={formData.userId}
              />
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2">작성일</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                name="title"
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
                placeholder="답변 내용을 작성하세요."
                name="answer"
                className="w-full h-96 focus-visible:outline-none"
                maxLength={255}
                onChange={handleChange}
                value={formData.answer}
              ></input>
            </td>
          </tr>
        </tbody>
      </table>
      <div className="w-full block mt-4">
        <button
          type="button"
          className="block w-full text-center bg-green-500 hover:bg-green-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
          onClick={handleClickAdd}
        >
          답변하기
        </button>
      </div>
    </div>
  );
};

export default QnaAnswerWritePage;
