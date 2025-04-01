import { useEffect, useContext, useState } from "react";
import { AuthContext } from "../App";
import axios from "axios";
import { updateUserInfo } from "../api/memberApi";
import AlertModal from "../components/AlertModal";

function StudentPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { userId, setUserId } = useContext(AuthContext);
  const [studentInfo, setStudentInfo] = useState(null);
  const [message, setMessage] = useState("");
  const [formData, setFormData] = useState({
    userName: "",
    userId: "",
    userBirth: "",
    userEmail: "",
    userPhone: "",
    userImgUrl: "",
    departmentName: "",
  });
  const localId = localStorage.getItem("id");

  useEffect(() => {
    if (userId) {
      fetchStudentInfo(userId);
    } else if (localId) {
      setUserId(localId);
      fetchStudentInfo(localId);
    }
  }, [userId, setUserId, localId]);

  const handleClose = () => {
    setIsModalOpen(false);
  };

  const fetchStudentInfo = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/${userId}`
      );
      setStudentInfo(response.data);
      setFormData({
        userName: response.data.userName,
        userId: response.data.userId,
        userBirth: response.data.userBirth,
        userEmail: response.data.userEmail,
        userPhone: response.data.userPhone,
        userImgUrl: response.data.userImgUrl,
        departmentName: response.data.departmentName || "",
      });
    } catch (error) {
      setMessage("학생 정보를 불러올 수 없습니다.");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      await updateUserInfo({
        userId: formData.userId,
        userEmail: formData.userEmail,
        userPhone: formData.userPhone,
      });
      setMessage("정보가 업데이트되었습니다.");
      setIsModalOpen(true); // 모달 열기
    } catch (error) {
      setMessage("정보 수정에 실패했습니다.");
      setIsModalOpen(true); // 실패해도 모달 열기
    }
  };

  return (
    <div className="flex justify-center min-h-screen bg-white">
      <AlertModal
        isOpen={isModalOpen}
        message={message} // 상태값 사용
        onClose={handleClose}
        type={message.includes("실패") ? "error" : "success"} // 에러/성공 구분
      />

      <div className="w-full max-w-5xl bg-white rounded-lg p-6">
        <h1 className="text-2xl font-bold text-center mb-6">학생 정보 조회</h1>
        {studentInfo ? (
          <form className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-10">
            <div className="col-span-1 flex justify-center">
              <img
                src={
                  formData.userImgUrl
                    ? `http://localhost:8080${formData.userImgUrl}`
                    : "/default-profile.jpg"
                }
                alt="Profile"
                className="w-32 h-32 mt-7 object-cover rounded-full border"
              />
            </div>

            <div className="col-span-1">
              <label className="block font-semibold p-2">이름</label>
              <input
                type="text"
                name="userName"
                value={formData.userName}
                readOnly
                className="bg-blue-50 border p-2 rounded w-full"
              />

              <label className="block font-semibold p-2 mt-2">학번</label>
              <input
                type="text"
                name="userId"
                value={formData.userId}
                readOnly
                className="bg-blue-50 border p-2 rounded w-full"
              />

              <label className="block font-semibold p-2 mt-2">생년월일</label>
              <input
                type="text"
                name="userBirth"
                value={formData.userBirth}
                readOnly
                className="bg-blue-50 border p-2 rounded w-full"
              />

              <label className="block font-semibold p-2 mt-2">이메일</label>
              <input
                type="email"
                name="userEmail"
                value={formData.userEmail}
                onChange={handleChange}
                className="w-full p-2 border rounded"
              />

              <label className="block font-semibold p-2 mt-2">전화번호</label>
              <input
                type="text"
                name="userPhone"
                value={formData.userPhone}
                onChange={handleChange}
                className="w-full p-2 border rounded"
              />

              <label className="block font-semibold p-2 mt-2">학과</label>
              <input
                type="text"
                name="departmentName"
                value={formData.departmentName}
                readOnly
                className="bg-blue-50 border p-2 rounded w-full"
              />
            </div>

            <div className="col-span-2 flex justify-center pt-10 mt-4">
              <button
                type="button"
                onClick={handleSave}
                className="bg-blue-500 text-white px-6 py-2 text-lg rounded-lg hover:bg-blue-800"
              >
                저장 ☑️
              </button>
            </div>
          </form>
        ) : (
          <p className="text-center text-gray-500">
            학생 정보를 불러오는 중...
          </p>
        )}
      </div>
    </div>
  );
}

export default StudentPage;
