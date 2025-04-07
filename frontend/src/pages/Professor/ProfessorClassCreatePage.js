import React, { useState, useEffect } from "react";
import {
  createClass,
  getCourses,
  getLectureRooms,
} from "../../api/professorClassApi";
import { useDispatch } from "react-redux";
import { showModal } from "../../slices/modalSlice";

const initialForm = {
  courseId: "",
  professorId: "",
  semester: "",
  day: "",
  startTime: "",
  endTime: "",
  capacity: 30,
  lectureRoomId: null,
};

const ProfessorClassCreatePage = ({ onSuccess, professorId }) => {
  const dispatch = useDispatch();

  const [form, setForm] = useState({ ...initialForm, professorId });
  const [courses, setCourses] = useState([]);
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    getCourses()
      .then((res) => setCourses(res.data))
      .catch(() => setCourses([]));
  }, []); 

  useEffect(() => {
    const { semester, day, startTime, endTime } = form;

    if (semester && day && startTime && endTime) {
      getLectureRooms({ semester, day, startTime, endTime })
        .then((res) => setRooms(res.data))
        .catch(() => setRooms([]));
    } else {
      setRooms([]);
    }
  }, [form.semester, form.day, form.startTime, form.endTime]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    try {
      const response = await createClass(form);
      dispatch(showModal(response.data || "강의 등록 완료"));
      onSuccess?.();
      setForm({ ...initialForm, professorId });
    } catch (err) {
      dispatch(
        showModal({
          message: err.response?.data || "강의 등록 실패",
          type: "error",
        })
      );
    }
  };

  const getSemesterOptions = () => {
    const year = new Date().getFullYear();
    return [`${year}-1`, `${year}-2`, `${year + 1}-1`, `${year + 1}-2`];
  };

  return (
    <div className="max-w-3xl mx-auto mt-6 p-6 bg-white shadow-md rounded-lg">
      <h2 className="text-xl font-bold mb-4 text-center">강의 등록</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="text-sm font-medium">과목 *</label>
          <select
            name="courseId"
            className="w-full p-2 border rounded"
            value={form.courseId || ""}
            onChange={handleChange}
          >
            <option value="">과목 선택</option>
            {courses.map((c) => (
              <option key={c.courseId} value={c.courseId}>
                {c.courseType === "MAJOR" ? "전공" : "교양"} - {c.courseName}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="text-sm font-medium">학기 *</label>
          <select
            name="semester"
            className="w-full p-2 border rounded"
            value={form.semester || ""}
            onChange={handleChange}
          >
            <option value="">학기 선택</option>
            {getSemesterOptions().map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="text-sm font-medium">요일 *</label>
          <select
            name="day"
            className="w-full p-2 border rounded"
            value={form.day}
            onChange={handleChange}
          >
            <option value="">요일 선택</option>
            {["월", "화", "수", "목", "금"].map((d) => (
              <option key={d} value={d}>
                {d}
              </option>
            ))}
          </select>
        </div>

        <div className="flex gap-2">
          <div>
            <label className="text-sm font-medium">시작 교시 *</label>
            <input
              type="number"
              name="startTime"
              min={1}
              max={10}
              className="w-full p-2 border rounded"
              value={form.startTime}
              onChange={handleChange}
              placeholder="예: 3"
            />
          </div>
          <div>
            <label className="text-sm font-medium">종료 교시 *</label>
            <input
              type="number"
              name="endTime"
              min={form.startTime || 1}
              max={10}
              className="w-full p-2 border rounded"
              value={form.endTime}
              onChange={handleChange}
              placeholder="예: 5"
            />
          </div>
        </div>

        <div className="md:col-span-2">
          <label className="text-sm font-medium">강의실 *</label>
          <select
            name="lectureRoomId"
            className="w-full p-2 border rounded"
            value={form.lectureRoomId || ""}
            onChange={handleChange}
          >
            <option value="">강의실 선택</option>
            {rooms.map((r) => (
              <option key={r.roomId} value={r.roomId}>
                {r.buildingName} {r.roomName}
              </option>
            ))}
          </select>
        </div>

        <div className="md:col-span-2">
          <label className="text-sm font-medium">수강 정원 *</label>
          <input
            type="number"
            name="capacity"
            min={20}
            max={50}
            className="w-full p-2 border rounded"
            value={form.capacity}
            onChange={handleChange}
          />
        </div>
      </div>

      <button
        onClick={handleSubmit}
        className="mt-6 w-full bg-blue-600 text-white py-3 rounded hover:bg-blue-800 transition"
      >
        강의 등록
      </button>
    </div>
  );
};

export default ProfessorClassCreatePage;
