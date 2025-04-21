import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { setUserId as setUserIdAction } from "../slices/authSlice";
import { fetchStudentGrades } from "../api/studentGradeApi";
import { convertGradeLabel } from "../util/gradeUtil";

const GradePage = () => {
  const dispatch = useDispatch();
  const userId = useSelector((state) => state.auth.userId);

  const [grades, setGrades] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const localId = localStorage.getItem("id");
    if (!userId && localId) {
      dispatch(setUserIdAction(localId));
      loadGrades();
    } else if (userId) {
      loadGrades();
    }
  }, [userId]);

  const loadGrades = async () => {
    try {
      const res = await fetchStudentGrades();
      setGrades(res.data);
    } catch {
      setMessage("성적 정보를 불러올 수 없습니다.");
    }
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">내 성적 조회</h2>
      </div>

      {message && (
        <div className="text-red-500 text-center font-medium mb-6">
          {message}
        </div>
      )}

      {/* 성적 테이블 */}
      <table className="min-w-full table-auto shadow-sm border border-gray-200 rounded-md text-sm">
        <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
          <tr className="text-center">
            <th className="py-3 px-4">강의명</th>
            <th className="py-3 px-4">학점</th>
            <th className="py-3 px-4">성적</th>
          </tr>
        </thead>
        <tbody className="text-center text-gray-700">
          {grades.length === 0 ? (
            <tr>
              <td colSpan={3} className="py-4 text-gray-400">
                조회할 성적이 없습니다.
              </td>
            </tr>
          ) : (
            grades.map((g, i) => (
              <tr key={i} className="hover:bg-gray-50 border-t">
                <td className="py-2 px-4">{g.courseName}</td>
                <td className="py-2 px-4">{g.credit}</td>
                <td className="py-2 px-4">
                  {g.grade ? convertGradeLabel(g.grade) : "미등록"}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default GradePage;