import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { setUserId as setUserIdAction } from "../slices/authSlice";
import { fetchStudentGrades, fetchStudentRecord } from "../api/studentGradeApi";
import { convertGradeLabel } from "../util/gradeUtil";

const GradePage = () => {
  const dispatch = useDispatch();
  const userId = useSelector((state) => state.auth.userId);

  const [grades, setGrades] = useState([]);
  const [record, setRecord] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const localId = localStorage.getItem("id");
    if (!userId && localId) {
      dispatch(setUserIdAction(localId));
      loadAllData();
    } else if (userId) {
      loadAllData();
    }
  }, [userId]);

  const loadAllData = async () => {
    try {
      const [gradesRes, recordRes] = await Promise.all([
        fetchStudentGrades(),
        fetchStudentRecord(),
      ]);
      setGrades(gradesRes.data);
      setRecord(recordRes.data);
    } catch {
      setMessage("성적 정보를 불러올 수 없습니다.");
    }
  };

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-10">
      {record && (
        <div className="bg-white shadow-md rounded-md p-6">
          <h2 className="text-2xl font-semibold text-gray-700 mb-4">
            {record.semester.year}년{" "}
            {record.semester.term === "FIRST" ? "1학기" : "2학기"} 성적
          </h2>
          <table className="min-w-full table-auto border border-gray-200 rounded-md text-sm">
            <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
              <tr className="text-center">
                <th className="py-3 px-4">연도</th>
                <th className="py-3 px-4">학기</th>
                <th className="py-3 px-4">신청학점</th>
                <th className="py-3 px-4">취득학점</th>
                <th className="py-3 px-4">평균평점</th>
              </tr>
            </thead>
            <tbody className="text-center text-gray-700">
              <tr className="hover:bg-gray-50 border-t">
                <td className="py-2 px-4">{record.semester.year}</td>
                <td className="py-2 px-4">
                  {record.semester.term === "FIRST" ? "1" : "2"}
                </td>
                <td className="py-2 px-4">{record.enrolled ?? "-"} </td>
                <td className="py-2 px-4">{record.earned ?? "-"} </td>
                <td className="py-2 px-4">{record.gpa?.toFixed(2) ?? "-"}</td>
              </tr>
            </tbody>
          </table>
        </div>
      )}

      <div className="bg-white shadow-md rounded-md p-6">
        <h2 className="text-2xl font-semibold text-gray-700 mb-4">
          과목별 성적
        </h2>

        {record && (
          <div className="flex items-center gap-2 text-gray-600 text-base mt-4 mb-6">
            <span className="text-gray-500 text-base">📅</span>
            <span className="font-semibold">
              {record.semester.year}년{" "}
              {record.semester.term === "FIRST" ? "1학기" : "2학기"}
            </span>
          </div>
        )}

        {message && (
          <div className="text-red-500 text-center font-medium mb-6">
            {message}
          </div>
        )}

        <table className="min-w-full table-auto border border-gray-200 rounded-md text-sm">
          <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
            <tr className="text-center">
              <th className="py-3 px-4">과목명</th>
              <th className="py-3 px-4">구분</th>
              <th className="py-3 px-4">학점</th>
              <th className="py-3 px-4">등급</th>
              <th className="py-3 px-4">평점</th>
            </tr>
          </thead>
          <tbody className="text-center text-gray-700">
            {grades.length === 0 ? (
              <tr>
                <td colSpan={5} className="py-4 text-gray-400">
                  조회할 성적이 없습니다.
                </td>
              </tr>
            ) : (
              grades.map((g, i) => (
                <tr key={i} className="hover:bg-gray-50 border-t">
                  <td className="py-2 px-4">{g.courseName}</td>
                  <td className="py-2 px-4">{g.courseType || "-"}</td>
                  <td className="py-2 px-4">{g.credit}</td>
                  <td className="py-2 px-4">
                    {g.grade ? convertGradeLabel(g.grade) : "미등록"}
                  </td>
                  <td className="py-2 px-4">
                    {g.gradePoint?.toFixed(1) ?? "-"}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default GradePage;
