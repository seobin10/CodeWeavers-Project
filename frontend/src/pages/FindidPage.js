import { useState } from "react";
import { findUserId } from "../api/memberApi";

function FindidPage() {
  const [formData, setFormData] = useState({
    userName: "",
    userPhone: "",
    userEmail: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleFindUserId = async (e) => {
    e.preventDefault();
    if (!formData.userName || !formData.userPhone || !formData.userEmail) {
      alert("이름, 전화번호, 이메일을 입력해주세요");
      return;
    }
    console.log("요청 데이터:", formData);
    try {
      const data = await findUserId(formData);
      console.log("응답데이터 :", data);
      alert(data.message || "학번 찾기 결과: " + data);
    } catch (error) {
      console.error("요청중 오류 발생 !:", error);
      alert("학번 찾기에 실패했습니다.");
    }
  };
  return (
    <div className="flex flex-col items-center justify-center py-10 bg-gray-100 w-full">
      <div className="w-full max-w-md bg-white p-3 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-center mb-1">학번 찾기</h2>

        <div className="absolute inset-0 flex flex-col items-center justify-center bg-gray-100 ">
          <img
            src="/images/eonLogo.jpg"
            alt="학교 로고"
            className="w-24 h-24 rounded-full mb-4"
          />

          <form onSubmit={handleFindUserId} className="space-y-3">
            <input
              type="text"
              name="userName"
              value={formData.userName}
              onChange={handleChange}
              placeholder="이름"
              className="w-full p-2 border rounded"
              requirednp
            />

            <input
              type="email"
              name="userEmail"
              value={formData.userEmail}
              onChange={handleChange}
              placeholder="이메일"
              className="w-full p-2 border rounded"
              required
            />

            <input
              type="text"
              name="userPhone"
              value={formData.userPhone}
              onChange={handleChange}
              placeholder="010-****-****"
              className="w-full p-2 border rounded"
              required
            />

            <button
              type="submit"
              className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-800"
            >
              학번 찾기
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default FindidPage;
