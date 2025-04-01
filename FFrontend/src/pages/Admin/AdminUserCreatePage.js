import React, { useState, useEffect, useRef, useContext } from "react";
import {
  createUser,
  getDepartments,
  uploadProfileImage,
} from "../../api/adminUserApi";
import { ModalContext } from "../../App";

const initialForm = {
  userId: "",
  userName: "",
  userPassword: "",
  userEmail: "",
  userPhone: "",
  userBirth: "",
  userRole: "STUDENT",
  departmentId: null,
  userImgUrl: "",
};

const AdminUserCreatePage = () => {
  const [form, setForm] = useState(initialForm);
  const [departments, setDepartments] = useState([]);
  const [emailId, setEmailId] = useState("");
  const [emailDomain, setEmailDomain] = useState("@naver.com");
  const [customEmailDomain, setCustomEmailDomain] = useState("");
  const [uploadMsg, setUploadMsg] = useState("");
  const [phoneParts, setPhoneParts] = useState({ part1: "010", part2: "", part3: "" });
  const [userIdMessage, setUserIdMessage] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const fileInputRef = useRef(null);
  const { showModal } = useContext(ModalContext);

  useEffect(() => {
    getDepartments()
      .then((res) => setDepartments(res.data))
      .catch(() => setDepartments([]));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => {
      const updated = { ...prev, [name]: value };
      if (name === "userId") {
        setUserIdMessage(/^\d{9}$/.test(value) ? "✔ 올바른 형식입니다." : "❌ 숫자 9자리여야 합니다.");
      }
      if (name === "userBirth" && /^\d{4}-\d{2}-\d{2}$/.test(value)) {
        const formatted = value.slice(2, 4) + value.slice(5, 7) + value.slice(8, 10);
        updated.userPassword = `${formatted}!`;
      }
      return updated;
    });
  };

  const handleEmailIdChange = (e) => {
    const emailId = e.target.value;
    setEmailId(emailId);
    const domain = emailDomain === "custom" ? customEmailDomain : emailDomain;
    setForm((prev) => ({ ...prev, userEmail: emailId + domain }));
  };

  const handleEmailDomainChange = (e) => {
    const selected = e.target.value;
    setEmailDomain(selected);
    const domain = selected === "custom" ? customEmailDomain : selected;
    setForm((prev) => ({ ...prev, userEmail: emailId + domain }));
  };

  const handleCustomDomainChange = (e) => {
    const value = e.target.value;
    setCustomEmailDomain(value);
    setForm((prev) => ({ ...prev, userEmail: emailId + "@" + value }));
  };

  const handlePhoneChange = (e) => {
    const { name, value } = e.target;
    setPhoneParts((prev) => {
      const updated = { ...prev, [name]: value };
      const phone = `${updated.part1}-${updated.part2}-${updated.part3}`;
      setForm((prevForm) => ({ ...prevForm, userPhone: phone }));
      return updated;
    });
  };

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    try {
      const res = await uploadProfileImage(file);
      setForm((prev) => ({ ...prev, userImgUrl: res.data }));
      setUploadMsg("✔ 이미지 업로드에 성공하였습니다.");
    } catch (err) {
      setUploadMsg("❌ 이미지 업로드에 실패하였습니다.");
    }
  };

  const handleSubmit = async () => {
    try {
      const response = await createUser(form);
      const msg = typeof response.data === "string" ? response.data : response.data.message ?? "응답 메시지를 확인할 수 없습니다.";
      showModal(msg);
      setForm(initialForm);
      setPhoneParts({ part1: "010", part2: "", part3: "" });
      setEmailDomain("@naver.com");
      setCustomEmailDomain("");
      setEmailId("");
      setUserIdMessage("");
      setUploadMsg("");
      fileInputRef.current.value = "";
    } catch (err) {
      const errorData = err.response?.data;
      let message = "알 수 없는 에러가 발생했습니다.";
      if (typeof errorData === "string") message = errorData;
      else if (typeof errorData === "object" && errorData.message) message = errorData.message;
      showModal(message);
    }
  };

  return (
    <div className="max-w-2xl mx-auto mt-8 p-6 bg-white shadow-md rounded-lg">
      <h2 className="text-3xl font-bold text-center mb-6">학생/교수 등록</h2>

      <div className="space-y-5">
        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">사용자 구분 *</label>
          <select name="userRole" className="w-full p-2 border rounded" value={form.userRole} onChange={handleChange}>
            <option value="STUDENT">학생</option>
            <option value="PROFESSOR">교수</option>
          </select>
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">학번 또는 ID *</label>
          <input name="userId" className="w-full p-2 border rounded" onChange={handleChange} value={form.userId} placeholder="예: 202500101" />
          <p className="text-sm mt-1 text-gray-600">{userIdMessage}</p>
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">이름 *</label>
          <input name="userName" className="w-full p-2 border rounded" onChange={handleChange} value={form.userName} placeholder="예: 홍길동" />
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">생년월일 *</label>
          <input name="userBirth" type="date" className="w-full p-2 border rounded" onChange={handleChange} value={form.userBirth} />
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">비밀번호 *</label>
          <div className="relative">
            <input
              name="userPassword"
              type={showPassword ? "text" : "password"}
              className="w-full p-2 border rounded pr-12"
              placeholder="기본값 : 생년월일 6자리 + !"
              value={form.userPassword}
              onChange={handleChange}
            />
            <button
              type="button"
              onClick={() => setShowPassword((prev) => !prev)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-blue-600 hover:text-blue-800"
            >
              {showPassword ? "🔓" : "🔒"}
            </button>
          </div>
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">이메일 *</label>
          <div className="flex flex-wrap gap-2">
            <input className="flex-1 min-w-[100px] p-2 border rounded" placeholder="아이디" value={emailId} onChange={handleEmailIdChange} />
            <span className="self-center">@</span>
            <select className="flex-1 min-w-[120px] p-2 border rounded" onChange={handleEmailDomainChange} value={emailDomain}>
              <option value="@naver.com">naver.com</option>
              <option value="@gmail.com">gmail.com</option>
              <option value="custom">직접 입력</option>
            </select>
            {emailDomain === "custom" && (
              <input
                className="flex-1 min-w-[120px] p-2 border rounded"
                placeholder="직접입력"
                value={customEmailDomain}
                onChange={handleCustomDomainChange}
              />
            )}
          </div>
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">전화번호 *</label>
          <div className="flex space-x-2">
            <input name="part1" maxLength={3} className="w-1/3 p-2 border rounded" value={phoneParts.part1} onChange={handlePhoneChange} />
            <input name="part2" maxLength={4} className="w-1/3 p-2 border rounded" value={phoneParts.part2} onChange={handlePhoneChange} />
            <input name="part3" maxLength={4} className="w-1/3 p-2 border rounded" value={phoneParts.part3} onChange={handlePhoneChange} />
          </div>
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">소속 학과 *</label>
          <select
            name="departmentId"
            className="w-full p-2 border rounded"
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

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">프로필 이미지</label>
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            className="block w-full text-sm text-gray-500
              file:mr-4 file:py-2 file:px-4
              file:rounded file:border-0
              file:text-sm file:font-semibold
              file:bg-blue-600 file:text-white
              hover:file:bg-blue-800
              border rounded"
            ref={fileInputRef}
          />
          {uploadMsg && <p className="text-sm mt-1">{uploadMsg}</p>}
        </div>

        <button
          onClick={handleSubmit}
          className="w-full mt-4 bg-blue-600 hover:bg-blue-800 text-white font-semibold py-3 rounded-lg transition"
        >
          등록
        </button>
      </div>
    </div>
  );
};

export default AdminUserCreatePage;
