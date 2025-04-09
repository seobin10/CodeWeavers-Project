import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AlertModal from "../components/AlertModal";

const CurrentPage = () => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(true);

  const handleClose = () => {
    setIsModalOpen(false);
    navigate("/main"); // 팝업창 닫은 후 메인으로 감
  };

  useEffect(() => {
    const handleEnterKey = (e) => {
      if (e.key === "Enter") {
        handleClose();
      }
    };

    window.addEventListener("keydown", handleEnterKey);

    return () => {
      window.removeEventListener("keydown", handleEnterKey);
    };
  });

  const alertMessage = `수강과목 중 강의평가가 입력되지 않은 과목이 있어 금학기 성적조회가 불가능합니다.\n강의평가를 입력하여 주시기 바랍니다.`;

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="w-full max-w-2xl bg-white shadow-lg rounded-lg p-8">
        <h1 className="text-3xl font-bold text-center mb-6">현재학기 성적 조회</h1>

        <AlertModal
          isOpen={isModalOpen}
          message={alertMessage}
          onClose={handleClose}
          type="error"
        />
      </div>
    </div>
  );
};

export default CurrentPage;
