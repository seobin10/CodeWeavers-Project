import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getMyClasses } from "../../api/professorClassApi";
import {
  getGradesByClass,
  registerGrade,
  updateGrade,
  deleteGrade,
} from "../../api/professorGradeApi";
import PageComponent from "../../components/PageComponent";
import { showModal } from "../../slices/modalSlice";

const ProfessorGradePage = () => {
  const dispatch = useDispatch();
  const userId = useSelector((state) => state.auth.userId);

  const [gradePage, setGradePage] = useState({
    dtoList: [],
    totalPage: 0,
    current: 1,
    totalCount: 0,
  });
  const [classes, setClasses] = useState([]);
  const [selectedClassId, setSelectedClassId] = useState(null);
  const [grades, setGrades] = useState([]);
  const [newGrades, setNewGrades] = useState({});

  useEffect(() => {
    if (userId) {
      getMyClasses(userId).then((res) => setClasses(res.data.dtoList || []));
    }
  }, [userId]);

  const fetchGrades = async (classId, page = 1) => {
    try {
      const res = await getGradesByClass(classId, userId, page);
      setGradePage(res.data);
      setGrades(res.data.dtoList);
      setSelectedClassId(classId);
    } catch {
      dispatch(
        showModal({
          message: "성적 조회에 실패했습니다.",
          type: "error",
        })
      );
    }
  };

  const handleGradeChange = (studentId, value) => {
    setNewGrades((prev) => ({ ...prev, [studentId]: value }));
  };

  const handleRegisterOrUpdate = async (studentId, enrollmentId, gradeId) => {
    const gradeValue = newGrades[studentId];
    if (!gradeValue)
      return dispatch(
        showModal({ message: "성적을 선택해주세요.", type: "error" })
      );

    try {
      const dto = { enrollmentId, grade: gradeValue };
      gradeId ? await updateGrade(dto) : await registerGrade(dto);
      dispatch(showModal(gradeId ? "성적 수정 완료" : "성적 등록 완료"));
      fetchGrades(selectedClassId);
    } catch {
      dispatch(
        showModal({ message: "성적 등록/수정에 실패했습니다.", type: "error" })
      );
    }
  };

  const handleDelete = async (gradeId) => {
    try {
      await deleteGrade(gradeId);
      dispatch(showModal("성적 초기화 완료"));
      fetchGrades(selectedClassId);
    } catch {
      dispatch(showModal({ message: "성적 초기화 실패", type: "error" }));
    }
  };

  const gradeMap = {
    A_PLUS: "A+",
    A0: "A",
    B_PLUS: "B+",
    B0: "B",
    C_PLUS: "C+",
    C0: "C",
    D_PLUS: "D+",
    D0: "D",
    F: "F",
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">성적 관리</h2>
      </div>
      {/* 강의 선택 */}
      <div className="mb-6">
        <select
          value={selectedClassId || ""}
          onChange={(e) => {
            const selected = e.target.value;
            if (selected) {
              fetchGrades(selected);
            } else {
              setGrades([]);
              setSelectedClassId(null);
              setGradePage({
                dtoList: [],
                totalPage: 0,
                current: 1,
                totalCount: 0,
              });
            }
          }}
          className="px-3 py-2 w-64 border border-gray-300 rounded-md shadow-sm text-sm text-gray-700 
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
        >
          <option value="">강의 선택</option>
          {classes.map((c) => (
            <option key={c.classId} value={c.classId}>
              {c.courseName} ({c.semester})
            </option>
          ))}
        </select>
      </div>
      {/* 성적 목록 */}
      <table className="min-w-full table-auto shadow-sm border border-gray-200 rounded-md text-sm">
        <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
          <tr className="text-center">
            <th className="py-3 px-4">학번</th>
            <th className="py-3 px-4">이름</th>
            <th className="py-3 px-4">과목명</th>
            <th className="py-3 px-4">학점</th>
            <th className="py-3 px-4">기존 성적</th>
            <th className="py-3 px-4">입력</th>
            <th className="py-3 px-4">관리</th>
          </tr>
        </thead>
        <tbody className="text-center text-gray-700">
          {grades.length === 0 ? (
            <tr>
              <td colSpan={7} className="py-4 text-gray-400">
                {selectedClassId
                  ? "성적 정보가 없습니다."
                  : "강의를 선택해주세요."}
              </td>
            </tr>
          ) : (
            grades.map((g) => (
              <tr key={g.studentId} className="hover:bg-gray-50 border-t">
                <td className="py-2 px-4">{g.studentId}</td>
                <td className="py-2 px-4">{g.studentName}</td>
                <td className="py-2 px-4">{g.courseName}</td>
                <td className="py-2 px-4">{g.credit}</td>
                <td className="py-2 px-4">
                  {g.grade ? gradeMap[g.grade] || g.grade : "미등록"}
                </td>
                <td className="py-2 px-4">
                  <select
                    value={newGrades[g.studentId] || ""}
                    onChange={(e) =>
                      handleGradeChange(g.studentId, e.target.value)
                    }
                    className="border px-2 py-1 rounded"
                  >
                    <option value="">선택</option>
                    {Object.entries(gradeMap).map(([value, label]) => (
                      <option key={value} value={value}>
                        {label}
                      </option>
                    ))}
                  </select>
                </td>
                <td className="py-2 px-4 space-x-2">
                  <button
                    onClick={() =>
                      handleRegisterOrUpdate(
                        g.studentId,
                        g.enrollmentId,
                        g.gradeId
                      )
                    }
                    className={`text-white px-2 py-1 rounded ${
                      g.grade
                        ? "bg-green-600 hover:bg-green-700"
                        : "bg-blue-600 hover:bg-blue-700"
                    }`}
                  >
                    {g.grade ? "수정" : "등록"}
                  </button>
                  {g.gradeId && (
                    <button
                      onClick={() => handleDelete(g.gradeId)}
                      className="text-white bg-red-600 px-2 py-1 rounded hover:bg-red-700"
                    >
                      초기화
                    </button>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <PageComponent
        currentPage={gradePage.current}
        totalPage={gradePage.totalPage}
        onPageChange={(page) => fetchGrades(selectedClassId, page)}
      />
    </div>
  );
};
export default ProfessorGradePage;
