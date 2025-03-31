import axios from "axios";
import React, { useContext, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { WaitModalClick } from "../../components/WaitModalClick";
import { AuthContext, ModalContext } from "../../App";

// 날짜 데이터 포맷팅
let date = new Date();
let year = date.getFullYear();
let month = date.getMonth() + 1;
if (month < 10) {
  month = "0" + month;
}
let day = date.getDate();
let today = year + "-" + month + "-" + day;

const QnaAnswerWritePage = ({ qno }) => {
  const { userId } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();
  const currentPage = location.state?.page;
  const { showModal } = useContext(ModalContext);
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
      showModal("내용을 입력해주세요");
      return e.preventDefault();
    } else {
      const obj = {
        answerId: null,
        questionId: parseInt(qno, 10),
        userId: userId,
        answer: formData.answer,
        answerDate: today,
      };
      await postAdd(obj);
      showModal("답변이 등록되었습니다.");
      await WaitModalClick();
      navigate("/main/qnaData", {
        state: { questionId: qno, page: currentPage },
      });
      setTimeout(() => {
        window.location.reload();
      }, 0);
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
      <div>
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

        <div className="w-full block">
          <br />
          <button
            type="button"
            className="block w-full text-center bg-green-500 hover:bg-green-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
            onClick={handleClickAdd}
          >
            답변하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default QnaAnswerWritePage;
