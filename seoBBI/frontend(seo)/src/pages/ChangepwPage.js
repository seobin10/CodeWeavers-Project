import React, { useState } from "react";
import axios from "axios";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getAuthHeader } from "../util/authHeader";

const ChangepwPage = () => {
  const { userId } = useSelector((state) => state.auth);
  const navigate = useNavigate();

  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  const handlePasswordChange = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      setMessage("❗ 새 비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      await axios.put(
        `/api/users/${userId}/password`,
        {
          currentPassword,
          newPassword,
        },
        { headers: getAuthHeader() }
      );

      setMessage("☑️ 비밀번호가 성공적으로 변경되었습니다.");
      setTimeout(() => {
        navigate("/main/student");
      }, 1500);
    } catch (error) {
      setMessage(
        "❗ 비밀번호 변경에 실패했습니다. 현재 비밀번호를 확인하세요."
      );
    }
  };

  return (
    <div className="flex justify-center items-start min-h-screen py-24">
      <div className="w-full max-w-xl bg-white p-10 shadow-md rounded-lg">
        <h2 className="text-2xl font-bold mb-8 text-center text-blue-800">
          비밀번호 변경
        </h2>
        <form onSubmit={handlePasswordChange} className="space-y-6">
          <div>
            <label className="block text-sm font-medium">현재 비밀번호</label>
            <input
              type="password"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              className="w-full border px-4 py-2 rounded mt-1"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium">새 비밀번호</label>
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              className="w-full border px-4 py-2 rounded mt-1"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium">
              새 비밀번호 확인
            </label>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full border px-4 py-2 rounded mt-1"
              required
            />
          </div>
          {message && (
            <p className="text-sm text-center text-red-600">{message}</p>
          )}
          <button
            type="submit"
            className="w-full bg-blue-600 hover:bg-blue-800 text-white py-3 rounded font-semibold"
          >
            비밀번호 변경
          </button>
        </form>
      </div>
    </div>
  );
};

export default ChangepwPage;
