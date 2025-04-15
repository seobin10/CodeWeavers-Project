import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";

const ResetPasswordPage = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(
        "http://localhost:8080/api/user/reset-password",
        {
          token,
          password,
        }
      );
      setMessage(res.data.message);
    } catch (err) {
      setMessage(
        "❌ 비밀번호 재설정 실패: " + (err.response?.data?.error || "오류 발생")
      );
    }
  };

  return (
    <div className="p-6 max-w-md mx-auto">
      <h2 className="text-2xl mb-4">🔐 비밀번호 재설정</h2>
      {message && <p className="mb-4 text-red-500">{message}</p>}
      <form onSubmit={handleSubmit}>
        <input
          type="password"
          placeholder="새 비밀번호 입력"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="border px-3 py-2 w-full mb-4"
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          비밀번호 재설정
        </button>
      </form>
    </div>
  );
};

export default ResetPasswordPage;
