import React, { useState, useEffect } from "react";
import {
  updateLectureRoom,
  getAllBuildings,
} from "../../api/adminLectureRoomApi";
import { useDispatch } from "react-redux";
import { showModal } from "../../slices/modalSlice"; // Redux 액션

const AdminLectureRoomEditPage = ({ roomId, rooms, onSuccess }) => {
  const [form, setForm] = useState({
    roomName: "",
    newName: "", // 수정할 강의실 이름
    newStatus: "", // 수정할 강의실 상태
  });
  const [buildings, setBuildings] = useState([]);
  const dispatch = useDispatch();

  useEffect(() => {
    // 건물 목록 가져오기
    getAllBuildings()
      .then((res) => setBuildings(res.data))
      .catch(() => setBuildings([]));

    // 강의실 정보 가져오기
    if (roomId && rooms) {
      const room = rooms.find((r) => r.roomId === roomId);
      if (room) {
        setForm({
          roomName: room.roomName,
          newName: room.roomName, // 초기값 설정
          newStatus: room.status,
        });
      } else {
        dispatch(
          showModal({ message: "강의실을 찾을 수 없습니다.", type: "error" })
        );
      }
    }
  }, [roomId, rooms, dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    try {
      await updateLectureRoom(roomId, form.newName, form.newStatus);
      dispatch(
        showModal({
          message: "강의실이 성공적으로 수정되었습니다.",
          type: "success",
        })
      );
      onSuccess(); // 부모에서 갱신
    } catch (err) {
      dispatch(
        showModal({
          message: "강의실 수정 중 오류가 발생했습니다.",
          type: "error",
        })
      );
    }
  };

  return (
    <div className="max-w-3xl mx-auto mt-8 p-6 bg-white shadow-md rounded-lg">
      <h2 className="text-2xl font-bold text-center mb-6">강의실 수정</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* 강의실 이름 */}
        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">
            강의실 이름 *
          </label>
          <input
            name="newName"
            className="w-full p-2 border rounded"
            onChange={handleChange}
            value={form.newName}
            placeholder="예: 101호"
          />
        </div>

        {/* 상태 선택 */}
        <div>
          <label className="block mb-1 text-sm font-medium text-gray-700">
            상태 *
          </label>
          <select
            name="newStatus"
            className="w-full p-2 border rounded"
            value={form.newStatus}
            onChange={handleChange}
          >
            <option value="AVAILABLE">AVAILABLE</option>
            <option value="UNAVAILABLE">UNAVAILABLE</option>
          </select>
        </div>

        {/* 수정 버튼 */}
        <div className="md:col-span-2">
          <button
            onClick={handleSubmit}
            className="w-full mt-4 bg-green-600 hover:bg-green-800 text-white font-semibold py-3 rounded-lg transition"
          >
            강의실 수정
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminLectureRoomEditPage;
