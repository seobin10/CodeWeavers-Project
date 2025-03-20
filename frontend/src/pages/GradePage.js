import { useEffect, useContext, useState } from "react";
import { AuthContext } from "../App";
import axios from "axios";

const GradePage = () => {
  const { userId, setUserId } = useContext(AuthContext);
  const [message, setMessage] = useState("");
  const [gradeInfo, setGradeInfo] = useState([]);

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
        `http://localhost:8080/api/students/grade/${userId}/grade`
      );
      console.log("Fetched data:", response.data);
      setGradeInfo(response.data);
    } catch (error) {
      setMessage("성적 정보를 불러올 수 없습니다.");
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="w-full max-w-2xl bg-white shadow-lg rounded-lg p-8">
        <h1 className="text-3xl font-bold text-center mb-6">성적 정보 조회</h1>
        {message && <p className="text-red-500 text-center">{message}</p>}

        {gradeInfo.length > 0 ? (
          gradeInfo.map((grade, i) => (
            <div key={i} className="mb-4 p-4 border rounded-lg bg-gray-50">
              <label className="block font-semibold p-2">강의 명</label>
              <input
                type="text"
                value={grade.courseName || ""}
                readOnly
                className="w-full p-2 border rounded bg-gray-200"
              />

              <label className="block font-semibold p-2 mt-2">학점</label>
              <input
                type="text"
                value={grade.credit || ""}
                readOnly
                className="w-full p-2 border rounded bg-gray-200"
              />

              <label className="block font-semibold p-2 mt-2">등급</label>
              <input
                type="text"
                value={grade.grade || ""}
                readOnly
                className="w-full p-2 border rounded bg-gray-200"
              />
            </div>
          ))
        ) : (
          <p className="text-center text-gray-500">
            성적 정보를 불러오는 중...
          </p>
        )}
      </div>
    </div>
  );
};

export default GradePage;
