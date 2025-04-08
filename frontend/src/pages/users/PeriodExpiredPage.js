import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

const PeriodExpiredPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const message =
    location.state?.message || "현재는 해당 기능을 이용할 수 있는 기간이 아닙니다.";

  return (
    <div className="max-w-2xl mx-auto px-8 py-32 bg-white shadow-md rounded-md mt-24">
      <div className="flex flex-col items-center justify-center text-center space-y-6">
        <img
          src="/images/eonLogo.jpg"
          alt="로고"
          className="w-24 h-24 rounded-full shadow mb-4"
        />
        <h1 className="text-3xl font-bold text-orange-600">이용 기간이 아닙니다.</h1>
        <p className="text-gray-700 text-lg">{message}</p>
        <button
          onClick={() => navigate("/main")}
          className="mt-6 inline-block bg-blue-600 text-white text-sm px-6 py-2 rounded-md hover:bg-blue-700 transition"
        >
          메인 페이지로 이동
        </button>
      </div>
    </div>
  );
};

export default PeriodExpiredPage;