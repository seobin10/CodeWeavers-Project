import React, { useEffect, useState } from "react";
import {
  getCoursesByFilter,
  createCourse,
  updateCourse,
  deleteCourse,
} from "../../api/adminCourseApi";
import { useDispatch } from "react-redux";
import { showModal } from "../../slices/modalSlice";
import useConfirmModal from "../../hooks/useConfirmModal";
import BaseModal from "../../components/BaseModal";
import PageComponent from "../../components/PageComponent";
import { getAllDepartments } from "../../api/adminDepartmentApi";

const AdminCoursePage = () => {
  const dispatch = useDispatch();
  const { openConfirm, ConfirmModalComponent } = useConfirmModal();

  const [courses, setCourses] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [selectedDept, setSelectedDept] = useState("");
  const [form, setForm] = useState({
    name: "",
    type: "MAJOR",
    credit: 1,
    year: 1,
    status: "AVAILABLE",
    departmentId: "",
  });
  const [editTarget, setEditTarget] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const fetchCourses = async () => {
    if (!selectedDept) return;
    const res = await getCoursesByFilter(selectedDept);
    setCourses(res.data);
  };

  const fetchDepartments = async () => {
    const res = await getAllDepartments();
    setDepartments(res.data);
    if (res.data.length > 0) setSelectedDept(String(res.data[0].departmentId));
  };

  useEffect(() => {
    fetchDepartments();
  }, []);

  useEffect(() => {
    setCurrentPage(1);
    fetchCourses();
  }, [selectedDept]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "type") {
      const isMajor = value === "MAJOR";
      setForm((prev) => ({
        ...prev,
        type: value,
        credit: isMajor ? 3 : 1,
        departmentId: isMajor ? prev.departmentId : "",
      }));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async () => {
    try {
      if (isEditMode && editTarget) {
        await updateCourse(editTarget.courseId, {
          newName: form.name,
          newCredit: Number(form.credit),
          newCourseYear: Number(form.year),
          newType: form.type,
          newStatus: form.status,
        });
        dispatch(showModal("과목 수정 완료"));
      } else {
        await createCourse({
          courseName: form.name,
          courseType: form.type,
          credit: Number(form.credit),
          courseYear: Number(form.year),
          departmentId:
            form.type === "MAJOR" && form.departmentId !== ""
              ? Number(form.departmentId)
              : null,
          status: form.status,
        });
        dispatch(showModal("과목 등록 완료"));
      }
      setIsModalOpen(false);
      setForm({
        name: "",
        type: "MAJOR",
        credit: 3,
        year: 1,
        status: "AVAILABLE",
      });
      setIsEditMode(false);
      setEditTarget(null);
      fetchCourses();
    } catch (err) {
      dispatch(
        showModal({
          message: err?.response?.data || "요청 처리 중 오류 발생",
          type: "error",
        })
      );
    }
  };

  const handleDelete = (course) => {
    openConfirm(`${course.courseName} 과목을 삭제하시겠습니까?`, async () => {
      try {
        await deleteCourse(course.courseId);
        dispatch(showModal("과목 삭제 완료"));
        fetchCourses();
      } catch (err) {
        dispatch(
          showModal({
            message: err?.response?.data || "삭제 중 오류 발생",
            type: "error",
          })
        );
      }
    });
  };

  const openEditModal = (course) => {
    setForm({
      name: course.courseName,
      type: course.courseType,
      credit: course.credit,
      year: course.courseYear,
      status: course.status,
      departmentId: course.departmentId || "",
    });
    setEditTarget(course);
    setIsEditMode(true);
    setIsModalOpen(true);
  };

  const paginatedCourses = courses.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <div className="w-full p-4 md:p-6 lg:p-8 mt-6">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">과목 현황</h2>
        <div className="flex gap-2">
          <select
            className="p-2 border rounded"
            value={selectedDept}
            onChange={(e) => setSelectedDept(e.target.value)}
          >
            {departments.map((d) => (
              <option key={d.departmentId} value={d.departmentId}>
                {d.departmentName}
              </option>
            ))}
            <option value="common">공통</option>
          </select>
          <button
            onClick={() => {
              setForm({
                name: "",
                type: "MAJOR",
                credit: 3,
                year: 1,
                status: "AVAILABLE",
                departmentId: "",
              });
              setIsEditMode(false);
              setIsModalOpen(true);
            }}
            className="px-4 py-2 bg-blue-700 text-white rounded hover:bg-blue-800"
          >
            과목 등록
          </button>
        </div>
      </div>

      <table className="min-w-full table-auto border border-gray-300 rounded text-sm">
        <thead className="bg-gray-50 text-gray-600 uppercase">
          <tr className="text-center">
            <th className="py-3 px-4">과목번호</th>
            <th className="py-3 px-4">과목명</th>
            <th className="py-3 px-4">구분</th>
            <th className="py-3 px-4">학점</th>
            <th className="py-3 px-4">학년</th>
            <th className="py-3 px-4">상태</th>
            <th className="py-3 px-4">관리</th>
          </tr>
        </thead>
        <tbody className="text-center text-gray-700">
          {courses.length === 0 ? (
            <tr>
              <td colSpan={7} className="py-4 text-gray-400">
                해당 학과의 과목이 없습니다.
              </td>
            </tr>
          ) : (
            paginatedCourses.map((c) => (
              <tr key={c.courseId} className="border-t hover:bg-gray-50">
                <td className="py-2 px-4">{c.courseId}</td>
                <td className="py-2 px-4">{c.courseName}</td>
                <td className="py-2 px-4">
                  {c.courseType === "MAJOR" ? "전공" : "교양"}
                </td>
                <td className="py-2 px-4">{c.credit}</td>
                <td className="py-2 px-4">{c.courseYear}</td>
                <td className="py-2 px-4">
                  {c.status === "AVAILABLE" ? "✅ 운영 중" : "❌ 운영 중지"}
                </td>
                <td className="py-2 px-4 space-x-2">
                  <button
                    onClick={() => openEditModal(c)}
                    className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded"
                  >
                    수정
                  </button>
                  <button
                    onClick={() => handleDelete(c)}
                    className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded"
                  >
                    삭제
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <PageComponent
        currentPage={currentPage}
        totalPage={Math.ceil(courses.length / itemsPerPage)}
        onPageChange={(page) => setCurrentPage(page)}
      />

      <BaseModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <h2 className="text-xl font-semibold mb-6 text-center">
          {isEditMode ? "과목 수정" : "과목 등록"}
        </h2>

        <div className="grid gap-4">
          <input
            type="text"
            name="name"
            value={form.name}
            onChange={handleChange}
            placeholder="과목명"
            className="w-full p-2 border rounded"
          />

          {!isEditMode && (
            <select
              name="type"
              value={form.type}
              onChange={handleChange}
              className="w-full p-2 border rounded"
            >
              <option value="MAJOR">전공</option>
              <option value="LIBERAL">교양</option>
            </select>
          )}

          {!isEditMode ? (
            form.type === "MAJOR" ? (
              <input
                type="number"
                name="credit"
                value={3}
                readOnly
                className="w-full p-2 border rounded bg-gray-100 cursor-not-allowed"
              />
            ) : (
              <select
                name="credit"
                value={form.credit}
                onChange={handleChange}
                className="w-full p-2 border rounded"
              >
                <option value={1}>1학점</option>
                <option value={2}>2학점</option>
              </select>
            )
          ) : (
            form.type === "LIBERAL" && (
              <select
                name="credit"
                value={form.credit}
                onChange={handleChange}
                className="w-full p-2 border rounded"
              >
                <option value={1}>1학점</option>
                <option value={2}>2학점</option>
              </select>
            )
          )}

          <input
            type="number"
            name="year"
            value={form.year}
            onChange={handleChange}
            placeholder="대상 학년"
            min={1}
            max={4}
            className="w-full p-2 border rounded"
          />

          <select
            name="status"
            value={form.status}
            onChange={handleChange}
            className="w-full p-2 border rounded"
          >
            <option value="AVAILABLE">운영 중</option>
            <option value="UNAVAILABLE">운영 중지</option>
          </select>

          <button
            onClick={handleSubmit}
            className={`mt-6 w-full ${
              isEditMode
                ? "bg-green-600 hover:bg-green-700"
                : "bg-blue-600 hover:bg-blue-700"
            } text-white py-2 rounded`}
          >
            {isEditMode ? "수정" : "등록"}
          </button>
        </div>
      </BaseModal>

      {ConfirmModalComponent}
    </div>
  );
};

export default AdminCoursePage;
