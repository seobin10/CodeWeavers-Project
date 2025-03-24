import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../App";
import { Link, useNavigate } from "react-router-dom";

// 날짜 데이터 포맷팅
let date = new Date();
let year = date.getFullYear();
let month = date.getMonth() + 1;
if (month<10){
    month = "0" + month;
}
let day = date.getDate();
let today = year + "-" + month + "-" + day;
console.log(today);

const QnaWritePage = () => {
  const navigate = useNavigate();
  const { userId, setUserId } = useContext(AuthContext);
  const [userInfo, setUserInfo] = useState(null);
  const [message, setMessage] = useState("");
  const [formData, setFormData] = useState({
    questionId: null,
    title: "",
    content: "",
    userName: "",
    createdAt: "",
    status: "OPEN",
    viewCount: 0,
  });

  const [userData, setUserData] = useState({
    userName: "",
    userId: "",
    userBirth: "",
    userEmail: "",
    userPhone: "",
    userImgUrl: "",
    departmentName: "",
  });

  const localId = localStorage.getItem("id");

  const postAdd = async (textObj) => {
    const headers = { "Content-Type": "application/json" };
    const res = await axios.post(
      "http://localhost:8080/api/user/qna/write?userId=" + userData.userId,
      textObj,
      {headers}
    );
    return res.data;
  };

  useEffect(() => {
    if (userId) {
      fetchUserInfo(userId);
    } else if (localId) {
      setUserId(localId);
      fetchUserInfo(localId);
    }
  }, [userId, setUserId, localId]);

  const fetchUserInfo = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/${userId}`
      );
      setUserInfo(response.data);
      setUserData({
        userName: response.data.userName,
        userId: response.data.userId,
        userBirth: response.data.userBirth,
        userEmail: response.data.userEmail,
        userPhone: response.data.userPhone,
        userImgUrl: response.data.userImgUrl,
        departmentName: response.data.departmentName || "",
      });
    } catch (error) {
      setMessage("유저 정보를 불러올 수 없습니다.");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleClickAdd = (e) => {
    const obj = {
        questionId: null,
        title: formData.title,
        content: formData.content,
        userName: userData.userName,
        userId: userData.userId,
        createdAt: today,
        status: "OPEN",
        viewCount: 0,
      };

    postAdd(obj).then(() => {
      alert(`질문이 등록되었습니다.`);
      navigate("/main/qnalist");
      window.location.reload();
    });
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-left mb-6">Q&A 등록</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      <table className="table-auto border-collapse border border-gray-400 w-full">
        <thead className="bg-gray-200">
          <tr>
            <th className="border border-gray-400 px-4 py-2">제목</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                placeholder="제목을 작성하세요"
                name="title"
                className="w-full focus-visible:outline-none"
                onChange={handleChange}
                value={formData.title}
              />
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2">작성자</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                name="userName"
                readOnly
                className="w-full focus-visible:outline-none"
                value={userData.userName}
              />
            </td>
          </tr>
          <tr>
            <th className="border border-gray-400 px-4 py-2">작성일</th>
            <td className="border border-gray-400 px-4 py-2 bg-white">
              <input
                name="title"
                value={today}
                readOnly
                className="w-full focus-visible:outline-none"
              />
            </td>
          </tr>
        </thead>
        <tbody>
          <tr className="w-full h-96 flex-auto">
            <td colSpan={2} className="p-4">
              <input
                placeholder="질문 내용을 작성하세요."
                name="content"
                className="w-full h-96 focus-visible:outline-none"
                maxLength={255}
                onChange={handleChange}
                value={formData.content}
              ></input>
            </td>
          </tr>
        </tbody>
      </table>
      <div>
          <br />
          <div className="flex float-right">
            <Link
              to="/main/qnalist"
              button
              className=" bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
            >
              돌아가기
            </Link>
            &nbsp;
            <Link
              to="/main/qnalist"
              button
              className=" bg-green-500 hover:bg-green-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
              onClick={handleClickAdd}
            >
              작성하기
            </Link>
          </div>
        {/* </p> */}
      </div>
    </div>
  );
};

export default QnaWritePage;
