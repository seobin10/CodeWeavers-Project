import React, { useState } from "react";
import { useSelector } from "react-redux";
import AlertModal from "../../components/AlertModal";
import { writeAns } from "../../api/qnaApi";

// 날짜 데이터 포맷팅
let date = new Date();
let year = date.getFullYear();
let month =
  date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
let today = year + "-" + month + "-" + day;

const QnaAnswerWritePage = ({ qno }) => {
  // 모달 데이터 정의(useState)
  const [alertModalOpen, setAlertModalOpen] = useState(false);
  const [type, setType] = useState(""); // 모달 스타일 정의
  const [msg, setMsg] = useState(""); // 모달 메시지
  const userId = useSelector((state) => state.auth.userId);

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

  // 클릭 시, 글이 작성되도록 하는 비동기 함수
  const handleClickAdd = async (e) => {
    if (!formData.answer.trim()) {
      setAlertData("error", "내용을 입력해주세요.")
      return e.preventDefault();
    }

    const data = {
      answerId: null,
      questionId: parseInt(qno, 10),
      userId: userId,
      answer: formData.answer,
      answerDate: today,
    };

    try {
      await writeAns(data);
      setAlertData("success", "답변이 등록되었습니다.")
    } catch (error) {
      setAlertData("error", "네트워크 상태가 좋지 않습니다.")

    }
  };

  // 모달 일괄 정의를 위한 함수
  const setAlertData = (modalType, modalMsg) => {
    setType(modalType);
    setMsg(modalMsg);
    setAlertModalOpen(true);
  };

  const handleClose = () => {
    setAlertModalOpen(false);
      setTimeout(() => window.location.reload(), 0);
  };


  return (
    <div className="border border-solid shadow-md p-10 rounded-md bg-white">
      <h1 className="text-md font-bold text-left mb-6">Q&A 답변</h1>
      <hr />
      <br />
      <table className="table-auto border-collapse border border-gray-400 w-full">
        <thead className="bg-blue-700">
          <tr>
            <th className="border border-gray-400 px-4 py-2 text-white">작성자ID</th>
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
            <th className="border border-gray-400 px-4 py-2 text-white">작성일</th>
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
          <tr className="w-full h-96 flex-auto shadow-md">
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
      <div className="mt-4 mb-8">
      <button
            className="float-right text-blue-500 hover:text-blue-700 text-lg font-semibold px-3 rounded transition"
            onClick={handleClickAdd}
          >
            📘 답변하기
          </button>
      </div>
      {/* 모달 */}
      <AlertModal
        isOpen={alertModalOpen}
        message={msg}
        onClose={() => handleClose()}
        type={type}
      />
    </div>
  );
};

export default QnaAnswerWritePage;
