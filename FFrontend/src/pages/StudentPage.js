import { useEffect, useContext, useState } from "react";
import { AuthContext } from "../App";
import axios from "axios";
import { updateUserInfo } from "../api/memberApi";

function StudentPage() {
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
      // formData에서 필요한 정보를 추출하여 업데이트 API에 전달합니다.
      await updateUserInfo({
        userId: formData.userId,
        userEmail: formData.userEmail,
        userPhone: formData.userPhone,
      });
      setMessage("정보가 업데이트되었습니다.");
    } catch (error) {
      setMessage("정보 수정에 실패했습니다.");
    }
  };

  return (
    <div className="flex justify-center min-h-screen bg-white">
      <div className="w-full max-w-5xl bg-white rounded-lg p-6">
        <h1 className="text-2xl font-bold text-center mb-6">학생 정보 조회</h1>
        {message && <p className="text-red-500 text-center">{message}</p>}
        {studentInfo ? (
          <form className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="col-span-1 flex justify-center">
              <img
                src={
                  formData.userImgUrl
                    ? `http://localhost:8080${formData.userImgUrl}`
                    : "/default-profile.jpg"
                }
                alt="Profile"
                className="w-32 h-32 object-cover rounded-full border"
              />
            </div>

            <div className="col-span-1">
              <label className="block font-semibold p-2">이름</label>
              <input
                type="text"
                name="userName"
                value={formData.userName}
                readOnly
                className="w-full p-2 border rounded bg-gray-100"
              />
              <label className="block font-semibold p-2 mt-2">학번</label>
              <input
                type="text"
                name="userId"
                value={formData.userId}
                readOnly
                className="w-full p-2 border rounded bg-gray-100"
              />
              <label className="block font-semibold p-2 mt-2">생년월일</label>
              <input
                type="text"
                name="userBirth"
                value={formData.userBirth}
                readOnly
                className="w-full p-2 border rounded bg-gray-100"
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
                className="w-full p-2 border rounded bg-gray-100"
              />
            </div>

            <div className="col-span-2 flex justify-center mt-4">
              <button
                type="button"
                onClick={handleSave}
                className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
              >
                저장
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
