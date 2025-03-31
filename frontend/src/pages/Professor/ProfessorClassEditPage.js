import React, { useState, useEffect, useContext } from "react";
import { getLectureRooms, updateClass } from "../../api/professorClassApi";
import { ModalContext } from "../../App";

const ProfessorClassEditPage = ({ classData, onSuccess, onClose }) => {
  const [form, setForm] = useState({ ...classData });
  const [lectureRooms, setLectureRooms] = useState([]);
  const { showModal } = useContext(ModalContext);

  useEffect(() => {
    getLectureRooms()
      .then((res) => setLectureRooms(res.data))
      .catch(() => setLectureRooms([]));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    try {
      const res = await updateClass(form);
      showModal(res.data);
      onSuccess();
      onClose();
    } catch (err) {
      const message = err.response?.data || "수정 중 오류가 발생했습니다.";
      showModal(message);
    }
  };

  return (
    <div className="max-w-2xl mx-auto mt-6 p-6 bg-white shadow-md rounded-lg">
      <h2 className="text-xl font-bold text-center mb-4">강의 정보 수정</h2>

      <div className="grid grid-cols-1 gap-5">
        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">강의 요일 *</label>
          <select name="day" value={form.day} onChange={handleChange} className="w-full p-2 border rounded">
            <option value="">요일 선택</option>
            <option value="월">월</option>
            <option value="화">화</option>
            <option value="수">수</option>
            <option value="목">목</option>
            <option value="금">금</option>
          </select>
        </div>

        <div className="flex gap-4">
          <div className="flex-1">
            <label className="block mb-1 text-sm font-medium text-gray-700">시작 교시 *</label>
            <input type="number" name="startTime" value={form.startTime} onChange={handleChange} className="w-full p-2 border rounded" />
          </div>
          <div className="flex-1">
            <label className="block mb-1 text-sm font-medium text-gray-700">종료 교시 *</label>
            <input type="number" name="endTime" value={form.endTime} onChange={handleChange} className="w-full p-2 border rounded" />
          </div>
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">수강 정원 *</label>
          <input type="number" name="capacity" value={form.capacity} onChange={handleChange} className="w-full p-2 border rounded" />
        </div>

        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">강의실 변경</label>
          <select
            name="lectureRoomId"
            value={form.lectureRoomId || ""}
            onChange={handleChange}
            className="w-full p-2 border rounded"
          >
            <option value="">변경 없음</option>
            {lectureRooms.map((r) => (
              <option key={r.roomId} value={r.roomId}>
                {r.buildingName} {r.roomName}
              </option>
            ))}
          </select>
        </div>

        <div>
          <button onClick={handleSubmit} className="w-full bg-blue-600 hover:bg-blue-800 text-white font-semibold py-3 rounded transition">
            수정 완료
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfessorClassEditPage;