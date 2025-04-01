import React, { useEffect, useState } from "react";
import {
  getGradesByClass,
  registerGrade,
  updateGrade,
  deleteGrade,
} from "../../api/professorGradeApi";
import { useSearchParams } from "react-router-dom";

const ProfessorGradePage = () => {
  const [searchParams] = useSearchParams();
  const classId = searchParams.get("classId");
  const [grades, setGrades] = useState([]);
  const [newGrades, setNewGrades] = useState({});

  const fetchGrades = async () => {
    try {
      const res = await getGradesByClass(classId);
      setGrades(res.data);
    } catch (err) {
      alert("성적 목록을 불러오는 데 실패했습니다.");
    }
  };

  useEffect(() => {
    if (classId) {
      fetchGrades();
    }
  }, [classId]);

  const handleChange = (studentId, value) => {
    setNewGrades((prev) => ({ ...prev, [studentId]: value }));
  };

  const handleRegister = async (studentId, enrollmentId) => {
    try {
      await registerGrade({ enrollmentId, grade: newGrades[studentId] });
      alert("성적 등록 완료");
      fetchGrades();
    } catch {
      alert("성적 등록 실패");
    }
  };

  const handleUpdate = async (studentId, enrollmentId) => {
    try {
      await updateGrade({ enrollmentId, grade: newGrades[studentId] });
      alert("성적 수정 완료");
      fetchGrades();
    } catch {
      alert("성적 수정 실패");
    }
  };

  const handleDelete = async (gradeId) => {
    try {
      await deleteGrade(gradeId);
      alert("성적 삭제 완료");
      fetchGrades();
    } catch {
      alert("성적 삭제 실패");
    }
  };

  if (!classId) {
    return (
      <div className="text-center text-red-500 mt-10">
        classId가 존재하지 않습니다. 올바른 접근이 아닙니다.
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-semibold mb-6">성적 관리</h2>
      <table className="min-w-full table-auto text-sm border">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-4 py-2">학생 ID</th>
            <th className="px-4 py-2">과목명</th>
            <th className="px-4 py-2">학점</th>
            <th className="px-4 py-2">성적</th>
            <th className="px-4 py-2">입력</th>
            <th className="px-4 py-2">관리</th>
          </tr>
        </thead>
        <tbody>
          {grades.map((g) => (
            <tr key={g.studentId} className="text-center border-t">
              <td className="px-4 py-2">{g.studentId}</td>
              <td className="px-4 py-2">{g.courseName}</td>
              <td className="px-4 py-2">{g.credit}</td>
              <td className="px-4 py-2">{g.grade || "미등록"}</td>
              <td className="px-4 py-2">
                <select
                  value={newGrades[g.studentId] || ""}
                  onChange={(e) => handleChange(g.studentId, e.target.value)}
                  className="border rounded px-2 py-1"
                >
                  <option value="">선택</option>
                  <option value="A_PLUS">A+</option>
                  <option value="A0">A</option>
                  <option value="B_PLUS">B+</option>
                  <option value="B0">B</option>
                  <option value="C_PLUS">C+</option>
                  <option value="C0">C</option>
                  <option value="D_PLUS">D+</option>
                  <option value="D0">D</option>
                  <option value="F">F</option>
                </select>
              </td>
              <td className="px-4 py-2 space-x-1">
                {g.grade ? (
                  <>
                    <button
                      onClick={() => handleUpdate(g.studentId, g.enrollmentId)}
                      className="bg-green-600 text-white px-2 py-1 rounded hover:bg-green-700"
                    >
                      수정
                    </button>
                    <button
                      onClick={() => handleDelete(g.gradeId)}
                      className="bg-red-600 text-white px-2 py-1 rounded hover:bg-red-700"
                    >
                      삭제
                    </button>
                  </>
                ) : (
                  <button
                    onClick={() => handleRegister(g.studentId, g.enrollmentId)}
                    className="bg-blue-600 text-white px-2 py-1 rounded hover:bg-blue-700"
                  >
                    등록
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProfessorGradePage;
