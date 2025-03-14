import { useEffect, useContext, useState } from "react";
import { AuthContext } from "../App";

const GradePage = () => {
  const { userId } = useContext(AuthContext);
  const [message, setMessage] = useState("");
  const [gradeInfo, setGradeInfo] = useState([]);

  useEffect(() => {
    if (userId) {
      console.log("학생 아이디 :", userId);
      fetchStudentInfo(userId);
    }
  }, [userId]);

  const fetchStudentInfo = async (userId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/grade/${userId}`
      );
      if (!response.ok) throw new Error("Failed to fetch student data");

      const data = await response.json();
      console.log("Fetched data:", data);
      setGradeInfo(data);
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
          <p className="text-center text-gray-500">성적 정보를 불러오는 중...</p>
        )}
      </div>
    </div>
  );
};

export default GradePage;
