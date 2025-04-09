import React, { useEffect, useState } from "react";
import axios from "axios";
import { getAuthHeader } from "../../util/authHeader";

function SendMessageCard() {
  const [phoneNumber, setPhoneNumber] = useState("");
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [isSent, setIsSent] = useState(false);
  const [msgLength, setMsgLength] = useState(0); 
  const handleSendMessage = async () => {
    if (!phoneNumber.trim() || !message.trim()) {
      alert("전화번호와 메시지를 모두 입력해주세요.");
      return;
    }

    try {
      const config = getAuthHeader();
      const response = await axios.post(
        "http://localhost:8080/api/professor/msg/send",
        null,
        {
          ...config,
          params: {
            to: phoneNumber,
            text: message,
          },
        }
      );

      console.log("메시지 전송 성공:", response.data);
      alert("메시지가 전송되었습니다!");
      setIsSent(true);
    } catch (error) {
      console.error("메시지 전송 실패:", error.response?.data || error.message);
      alert("메시지 전송에 실패했습니다.");
    }
  };

  // 글자 수 카운트
  const handleMsg = () => {
    setMsgLength(document.getElementById("msg").value.length);
    console.log(msgLength);
    if(msgLength > 45) {
      setError("글자 수는 45자가 최대입니다.");
    }
  };

  return (
    <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-md mt-10 space-y-6">
      <div>
        <label className="block mb-2 text-sm font-medium text-gray-700">
          휴대전화 번호
        </label>
        <input
          type="text"
          placeholder="-를 뺀 학생 전화번호 입력하세요. 예> 01098761234"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
          className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div>
        <label className="block mb-2 text-sm font-medium text-gray-700">
          메시지 내용
        </label>
        <textarea
          placeholder="45자 이하의 메시지를 입력하세요."
          value={message}
          onChange={(e) => {
            setMessage(e.target.value);
            handleMsg();
          }}
          className="w-full px-4 py-2 border border-gray-300 rounded-md h-32 resize-none focus:outline-none focus:ring-2 focus:ring-blue-500"
          maxLength={45}
          id="msg"
        />
        <p className="text-right text-gray-400">{msgLength} / 45</p>
      </div>

      <button
        onClick={handleSendMessage}
        className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-md transition"
      >
        메시지 전송
      </button>
      {error && <p className="text-red-500 text-center">{error}</p>}
      {isSent && (
        <p className="text-green-600 text-center font-medium">
          메시지 전송 완료!
        </p>
      )}
    </div>
  );
}

export default SendMessageCard;
