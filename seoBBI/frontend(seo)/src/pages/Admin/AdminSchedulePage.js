import React, { useEffect, useState } from "react";
import { getScheduleByType, saveSchedule } from "../../api/adminScheduleApi";
import BaseModal from "../../components/BaseModal";
import { showModal } from "../../slices/modalSlice";
import { useDispatch } from "react-redux";

const SCHEDULE_TYPES = [
  { type: "ENROLL", label: "수강신청 기간" },
  { type: "CLASS", label: "강의등록 기간" },
  { type: "GRADE", label: "성적입력 기간" },
];

const AdminSchedulePage = () => {
  const dispatch = useDispatch();

  const [schedules, setSchedules] = useState([]);
  const [selected, setSelected] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchSchedules = async () => {
    try {
      const results = await Promise.all(
        SCHEDULE_TYPES.map(async ({ type }) => {
          try {
            const res = await getScheduleByType(type);
            return res.data;
          } catch (err) {
            // 404 등 에러 발생 시 기본 값 리턴
            return {
              scheduleType: type,
              startDate: "",
              endDate: "",
              description: "",
            };
          }
        })
      );
      setSchedules(results);
    } catch (err) {
      dispatch(
        showModal({
          message: "학사 일정을 불러오지 못했습니다.",
          type: "error",
        })
      );
    }
  };

  useEffect(() => {
    fetchSchedules();
  }, []);

  const handleOpenModal = (schedule) => {
    setSelected({ ...schedule });
    setIsModalOpen(true);
  };

  const handleSave = async () => {
    try {
      await saveSchedule(selected);
      dispatch(showModal("일정이 저장되었습니다."));
      setIsModalOpen(false);
      fetchSchedules();
    } catch (err) {
      dispatch(showModal({ message: "일정 저장 중 오류 발생", type: "error" }));
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSelected((prev) => ({ ...prev, [name]: value }));
  };

  const getLabel = (type) =>
    SCHEDULE_TYPES.find((t) => t.type === type)?.label || type;

  return (
    <div className="max-w-3xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <h2 className="text-2xl font-semibold mb-6">학사일정 관리</h2>

      <table className="min-w-full table-auto shadow-sm border border-gray-200 rounded-md">
        <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
          <tr>
            <th className="py-3 px-4">일정 종류</th>
            <th className="py-3 px-4">시작일</th>
            <th className="py-3 px-4">종료일</th>
            <th className="py-3 px-4">설명</th>
            <th className="py-3 px-4">관리</th>
          </tr>
        </thead>
        <tbody className="text-gray-700 text-sm text-center">
          {schedules.map((s) => (
            <tr key={s.scheduleType} className="border-b hover:bg-gray-50">
              <td className="py-3 px-4">{getLabel(s.scheduleType)}</td>
              <td className="py-3 px-4">{s.startDate}</td>
              <td className="py-3 px-4">{s.endDate}</td>
              <td className="py-3 px-4">{s.description}</td>
              <td className="py-3 px-4">
                <button
                  onClick={() => handleOpenModal(s)}
                  className="text-white bg-blue-600 px-3 py-1 rounded hover:bg-blue-700 transition"
                >
                  수정
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <BaseModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        {selected && (
          <div className="space-y-4">
            <h3 className="text-xl font-semibold mb-2">
              {getLabel(selected.scheduleType)} 수정
            </h3>
            <div className="space-y-2">
              <label className="block">시작일</label>
              <input
                type="date"
                name="startDate"
                value={selected.startDate}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
              />
            </div>
            <div className="space-y-2">
              <label className="block">종료일</label>
              <input
                type="date"
                name="endDate"
                value={selected.endDate}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
              />
            </div>
            <div className="space-y-2">
              <label className="block">설명</label>
              <input
                type="text"
                name="description"
                value={selected.description}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
              />
            </div>
            <div className="flex justify-end mt-4 space-x-2">
              <button
                onClick={() => setIsModalOpen(false)}
                className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
              >
                취소
              </button>
              <button
                onClick={handleSave}
                className="px-4 py-2 bg-blue-700 text-white rounded hover:bg-blue-800"
              >
                저장
              </button>
            </div>
          </div>
        )}
      </BaseModal>
    </div>
  );
};

export default AdminSchedulePage;
