import React, { useState, useEffect } from "react";
import { createUser, getDepartments } from "../../api/createUserApi";

const AdminUserCreatePage = () => {
  const [form, setForm] = useState({
    userId: "",
    userName: "",
    userPassword: "",
    userEmail: "",
    userPhone: "",
    userBirth: "",
    userRole: "STUDENT",
    departmentId: null,
  });

  const [departments, setDepartments] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    getDepartments()
      .then((res) => {
        setDepartments(res.data);
      })
      .catch(() => setDepartments([]));
  }, []);
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    try {
      await createUser(form);
      alert("✅ 사용자 생성 완료!");
    } catch (err) {
      const errorMsg = "❌ " + (err.response?.data || err.message);
      alert(errorMsg); 
    }
  };

  return (
    <div className="max-w-xl mx-auto mt-10 p-6 bg-white shadow rounded">
      <h2 className="text-2xl font-bold mb-4 text-center">사용자 등록</h2>

      <div className="space-y-3">
        <input
          name="userId"
          placeholder="학번 또는 ID"
          className="w-full p-2 border"
          onChange={handleChange}
        />
        <input
          name="userName"
          placeholder="이름"
          className="w-full p-2 border"
          onChange={handleChange}
        />
        <input
          name="userPassword"
          placeholder="비밀번호"
          type="password"
          className="w-full p-2 border"
          onChange={handleChange}
        />
        <input
          name="userEmail"
          placeholder="이메일"
          className="w-full p-2 border"
          onChange={handleChange}
        />
        <input
          name="userPhone"
          placeholder="전화번호"
          className="w-full p-2 border"
          onChange={handleChange}
        />
        <input
          name="userBirth"
          placeholder="생년월일 (YYYY-MM-DD)"
          className="w-full p-2 border"
          onChange={handleChange}
        />

        {/* 사용자 역할 선택 */}
        <select
          name="userRole"
          className="w-full p-2 border"
          value={form.userRole}
          onChange={handleChange}
        >
          <option value="STUDENT">학생</option>
          <option value="PROFESSOR">교수</option>
        </select>

        {/* 학과 선택 */}
        <select
          name="departmentId"
          className="w-full p-2 border"
          value={form.departmentId !== null ? String(form.departmentId) : ""}
          onChange={(e) => {
            const selected = e.target.value;
            setForm((prev) => ({
              ...prev,
              departmentId: selected !== "" ? parseInt(selected, 10) : null,
            }));
          }}
        >
          <option value="">학과 선택</option>
          {departments.map((d) => (
            <option key={d.departmentId} value={String(d.departmentId)}>
              {d.departmentName}
            </option>
          ))}
        </select>
      </div>

      <button
        onClick={handleSubmit}
        className="mt-6 w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-800"
      >
        사용자 생성
      </button>

      {message && <p className="mt-4 text-center text-sm">{message}</p>}
    </div>
  );
};

export default AdminUserCreatePage;
