import axios from "axios";
import React, { useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const QnaAnswerDeletePage = ({ qno }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentPage = location.state?.page;
  const handleClickDelete = useCallback(async () => {
    const reloadPage = () => {
      navigate("/main/qnaData", {
        state: { questionId: qno, page: currentPage },
      });
      setTimeout(async () => {
        window.location.reload();
      }, 0);
    };
    try {
      await axios.delete(`http://localhost:8080/api/admin/ans/delete/${qno}`);
      reloadPage();
    } catch (error) {
      reloadPage();
    }
  }, [qno, navigate, currentPage]);

  return (
    <div className="border border-solid shadow-md p-10 rounded-md bg-white">
      <div>
        <h1 className="text-2xl text-center font-bold mb-6">
          답변을 삭제하시겠습니까?
        </h1>
        <div className="w-full block">
          <br />
          <button
            type="button"
            className="block w-full text-center bg-red-500 hover:bg-red-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
            onClick={handleClickDelete}
          >
            답변 삭제 ❌
          </button>
        </div>
      </div>
    </div>
  );
};

export default QnaAnswerDeletePage;
