import React from "react";
import { useNavigate } from "react-router-dom";

const FloatingPopup = ({ subjects }) => {
  const navigate = useNavigate();

  return (
    <div className="fixed top-1/2 translate-y-1/2 right-0 z-50 bg-white shadow-lg rounded-xl p-4 w-72">
      <div className="flex items-center justify-between mb-3">
        <h4 className="font-semibold text-base"> 📚 담은 과목 </h4>
        <button
          onClick={() => navigate("/main/schedule")}
          className="text-xl hover:scale-110 transition"
          title="시간표 확인하기"
        >
          📘📘📘
        </button>
      </div>
      <ul className="text-sm max-h-40 overflow-y-auto pr-1 mb-2">
        {subjects.map((subj, idx) => (
          <li key={idx}>
            • {subj.courseName} ({subj.classDay},{subj.classStartPeriod} ~{" "}
            {subj.classEndPeriod}교시)
          </li>
        ))}
      </ul>
      <p className="text-xs text-gray-500"> 총 {subjects.length} 과목 담김 </p>
    </div>
  );
};

export default FloatingPopup;
