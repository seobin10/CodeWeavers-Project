import { useEffect, useContext, useState } from "react";
import { AuthContext } from "../App";

function StudentPage() {
  const { userId } = useContext(AuthContext);
  const [studentInfo, setStudentInfo] = useState(null);
  const [message, setMessage] = useState("");
  const [formData, setFormData] = useState({
    userName: "",
    userId: "",
    userBirth: "",
    userEmail: "",
    userPhone: "",
    departmentName: "",
  });

  useEffect(() => {
    if (userId) {
      fetchStudentInfo(userId);
    }
  }, [userId]);

  const fetchStudentInfo = async (userId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/user/${userId}` 
      );
      if (!response.ok) throw new Error("Failed to fetch student data");

      const data = await response.json();
      setStudentInfo(data);
      setFormData({
        userName: data.userName,
        userId: data.userId,
        userBirth: data.userBirth,
        userEmail: data.userEmail,
        userPhone: data.userPhone,
        departmentName: data.departmentName || "", 
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
      const response = await fetch(
        `http://localhost:8080/api/user/update`, 
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            userId: formData.userId, // userId는 변경 불가
            userEmail: formData.userEmail, // 변경 가능
            userPhone: formData.userPhone, // 변경 가능
          }),
        }
      );
      if (!response.ok) throw new Error("수정에 실패했습니다.");

      setMessage("정보가 성공적으로 수정되었습니다.");
    } catch (error) {
      setMessage("수정 중 오류가 발생했습니다.");
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
                src="/path-to-profile-image.jpg"
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
