import React, { useEffect, useRef } from "react";

const AlertModal = ({ isOpen, message, onClose }) => {
  const buttonRef = useRef(null);

  useEffect(() => {
    if (!isOpen) return;

    const handleKeyDown = (e) => {
      if (e.key === "Enter") {
        e.preventDefault(); // ✅ 기본 동작 방지
        onClose(); // ✅ 닫기 실행
      }
    };

    window.addEventListener("keydown", handleKeyDown, { once: true }); // ✅ 한 번만 실행

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg text-center w-80">
        <p className="mb-4">{message}</p>
        <button
          ref={buttonRef}
          autoFocus
          onClick={onClose}
          className="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded"
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default AlertModal;
