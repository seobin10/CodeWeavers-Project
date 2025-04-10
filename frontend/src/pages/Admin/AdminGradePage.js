import React, { useEffect, useState } from "react";
import { getAllSemesters } from "../../api/adminScheduleApi";
import { getDepartments } from "../../api/adminUserApi"; 
import { finalizeGradesByDepartment } from "../../api/adminGradeApi";
import { useDispatch } from "react-redux";
import { showModal } from "../../slices/modalSlice";

const AdminGradePage = () => {
  const dispatch = useDispatch();
  const [semesters, setSemesters] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [selectedSemesterId, setSelectedSemesterId] = useState("");
  const [selectedDepartmentId, setSelectedDepartmentId] = useState("");

  useEffect(() => {
    getAllSemesters().then((res) => setSemesters(res.data));
    getDepartments().then((res) => setDepartments(res.data));
  }, []);

  const handleFinalize = async () => {
    try {
      if (!selectedSemesterId || !selectedDepartmentId) {
        dispatch(showModal({ message: "학기와 학과를 모두 선택해주세요.", type: "error" }));
        return;
      }
      await finalizeGradesByDepartment(selectedSemesterId, selectedDepartmentId);
      dispatch(showModal("성적 집계가 완료되었습니다."));
    } catch (e) {
      dispatch(showModal({ message: "성적 집계 실패", type: "error" }));
    }
  };

  return (
    <div className="max-w-3xl mx-auto mt-10 bg-white p-6 rounded shadow space-y-6">
      <h2 className="text-2xl font-semibold text-gray-700">성적 집계</h2>

      <div className="space-y-4">
        <select
          value={selectedSemesterId}
          onChange={(e) => setSelectedSemesterId(e.target.value)}
          className="w-full border px-3 py-2 rounded"
        >
          <option value="">학기 선택</option>
          {semesters.map((s) => (
            <option key={s.semesterId} value={s.semesterId}>
              {s.year}년 {s.term === "FIRST" ? "1학기" : "2학기"}
            </option>
          ))}
        </select>

        <select
          value={selectedDepartmentId}
          onChange={(e) => setSelectedDepartmentId(e.target.value)}
          className="w-full border px-3 py-2 rounded"
        >
          <option value="">학과 선택</option>
          {departments.map((d) => (
            <option key={d.departmentId} value={d.departmentId}>
              {d.departmentName}
            </option>
          ))}
        </select>

        <button
          onClick={handleFinalize}
          className="w-full bg-blue-700 text-white py-2 rounded hover:bg-blue-800 transition"
        >
          성적 집계 실행
        </button>
      </div>
    </div>
  );
};

export default AdminGradePage;