import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import axios from "axios";
import { updateUserInfo } from "../api/memberApi";
import { getAuthHeader } from "../util/authHeader";

function StudentPage() {
  const userId = useSelector((state) => state.auth?.userId);
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

  useEffect(() => {
    if (userId) {
      fetchStudentInfo(userId);
    }
  }, [userId]);

  const fetchStudentInfo = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/${userId}`,
        getAuthHeader()
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
    } catch (error) {
      setMessage("정보 수정에 실패했습니다.");
    }
  };

  const InputField = ({ label, name, value, readOnly = false, onChange }) => (
    <div className="relative mb-6">
      <input
        type="text"
        name={name}
        value={value}
        readOnly={readOnly}
        onChange={onChange}
        className={`peer w-full p-3 pt-5 border rounded-md bg-transparent placeholder-transparent
          ${
            readOnly
              ? "bg-gray-100 text-gray-500 cursor-not-allowed"
              : "focus:outline-none focus:ring-2 focus:ring-blue-500"
          }`}
        placeholder={label}
      />
      <label
        htmlFor={name}
        className={`absolute left-3 top-3 text-gray-500 text-sm transition-all duration-200
          peer-placeholder-shown:top-3.5 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400
          peer-focus:top-1 peer-focus:text-sm peer-focus:text-blue-600
          ${
            readOnly
              ? ""
              : "after:content-['(수정_가능)'] after:ml-1 after:text-xs after:text-green-600"
          }
        `}
      >
        {label}
      </label>
    </div>
  );

  return (
    <div className="flex justify-center min-h-screen bg-gradient-to-b from-blue-50 to-white p-4">
      <div className="w-full max-w-4xl bg-white shadow-lg rounded-xl p-8">
        <h1 className="text-3xl font-bold text-center mb-6 text-blue-600">
          학생 정보 조회 및 수정
        </h1>
        {message && (
          <div className="text-center text-sm text-red-600 mb-4">{message}</div>
        )}
        {studentInfo ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="flex flex-col items-center">
              <img
                src={
                  formData.userImgUrl
                    ? `http://localhost:8080${formData.userImgUrl}`
                    : "/default-profile.jpg"
                }
                alt="Profile"
                className="w-36 h-36 object-cover rounded-full border-4 border-blue-300 shadow"
              />
              <p className="mt-2 text-sm text-gray-500">프로필 사진</p>
            </div>

            <div>
              <InputField
                label="이름"
                name="userName"
                value={formData.userName}
                readOnly
              />
              <InputField
                label="학번"
                name="userId"
                value={formData.userId}
                readOnly
              />
              <InputField
                label="생년월일"
                name="userBirth"
                value={formData.userBirth}
                readOnly
              />
              <InputField
                label="이메일"
                name="userEmail"
                value={formData.userEmail}
                onChange={handleChange}
              />
              <InputField
                label="전화번호"
                name="userPhone"
                value={formData.userPhone}
                onChange={handleChange}
              />
              <InputField
                label="학과"
                name="departmentName"
                value={formData.departmentName}
                readOnly
              />
            </div>

            <div className="col-span-2 flex justify-center mt-4">
              <button
                type="button"
                onClick={handleSave}
                className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-2 rounded-lg shadow-md transition"
              >
                정보 수정하기
              </button>
            </div>
          </div>
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
