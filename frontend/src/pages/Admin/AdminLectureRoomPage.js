import React, { useState, useEffect } from "react";
import {
  getAllBuildings,
  getLectureRoomsByBuilding,
  getLectureRoomUsage,
} from "../../api/adminLectureRoomApi";
import PageComponent from "../../components/PageComponent";
import BaseModal from "../../components/BaseModal";
import AdminLectureRoomCreatePage from "./AdminLectureRoomCreatePage";
import AdminLectureRoomEditPage from "./AdminLectureRoomEditPage"; // 수정 페이지 추가

const AdminLectureRoomPage = () => {
  const [buildings, setBuildings] = useState([]);
  const [selectedBuildingId, setSelectedBuildingId] = useState("");
  const [selectedBuildingName, setSelectedBuildingName] = useState("");
  const [rooms, setRooms] = useState([]);
  const [selectedRoomUsage, setSelectedRoomUsage] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedRoomName, setSelectedRoomName] = useState("");
  const [occupiedRoomIds, setOccupiedRoomIds] = useState([]);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false); // 수정 모달 상태

  const [currentPage, setCurrentPage] = useState(1);
  const roomsPerPage = 20;

  const periodTimes = [
    { label: "1교시", time: "09:00 ~ 09:50" },
    { label: "2교시", time: "10:00 ~ 10:50" },
    { label: "3교시", time: "11:00 ~ 11:50" },
    { label: "4교시", time: "12:00 ~ 12:50" },
    { label: "5교시", time: "13:00 ~ 13:50" },
    { label: "6교시", time: "14:00 ~ 14:50" },
    { label: "7교시", time: "15:00 ~ 15:50" },
    { label: "8교시", time: "16:00 ~ 16:50" },
    { label: "9교시", time: "17:00 ~ 17:50" },
    { label: "10교시", time: "18:00 ~ 18:50" },
  ];

  const getPeriodRangeText = (start, end) => {
    const startLabel = `${start}교시`;
    const endLabel = `${end}교시`;

    const startTime = periodTimes[start - 1]?.time?.split(" ~ ")[0] || "";
    const endTime = periodTimes[end - 1]?.time?.split(" ~ ")[1] || "";

    return `${startLabel} ~ ${endLabel} (${startTime} ~ ${endTime})`;
  };

  const paginatedRooms = rooms.slice(
    (currentPage - 1) * roomsPerPage,
    currentPage * roomsPerPage
  );

  useEffect(() => {
    getAllBuildings()
      .then((res) => {
        const buildingList = res.data;
        setBuildings(buildingList);

        if (buildingList.length > 0) {
          const defaultBuildingId = buildingList[0].buildingId;
          handleBuildingChange({ target: { value: defaultBuildingId } });
        }
      })
      .catch(() => setBuildings([]));
  }, []);

  const handleBuildingChange = async (e) => {
    const buildingId = e.target.value;
    setSelectedBuildingId(buildingId);
    setCurrentPage(1);
    try {
      const res = await getLectureRoomsByBuilding(buildingId);
      const roomsData = res.data.sort((a, b) =>
        a.roomName.localeCompare(b.roomName)
      );
      setRooms(roomsData);

      const usageResults = await Promise.all(
        roomsData.map((room) =>
          getLectureRoomUsage(room.roomId).then((res) => ({
            roomId: room.roomId,
            isOccupied: res.data.length > 0,
          }))
        )
      );

      const occupied = usageResults
        .filter((res) => res.isOccupied)
        .map((res) => res.roomId);

      setOccupiedRoomIds(occupied);
    } catch {
      setRooms([]);
    }
  };

  const handleRoomClick = async (roomId, roomName) => {
    const matchedRoom = rooms.find((room) => room.roomId === roomId);
    const buildingName = matchedRoom?.buildingName || "";

    try {
      const res = await getLectureRoomUsage(roomId);
      const sorted = res.data.sort((a, b) => {
        if (a.day !== b.day) return a.day.localeCompare(b.day);
        return a.startTime - b.startTime;
      });
      setSelectedRoomUsage(sorted);
      setSelectedRoomName(roomName);
      setSelectedBuildingName(buildingName);
      setIsModalOpen(true);
    } catch {
      setSelectedRoomUsage([]);
      setSelectedBuildingName(buildingName);
    }
  };

  const handleEditClick = () => {
    setIsEditModalOpen(true); // 수정 모달 열기
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">
          강의실 사용 현황
        </h2>
        {/* 강의실 등록 버튼 추가 */}
        <button
          onClick={() => setIsCreateModalOpen(true)} // 생성 모달 열기
          className="px-4 py-2 bg-blue-700 text-white rounded hover:bg-blue-800 transition"
        >
          강의실 등록
        </button>
      </div>

      <div className="mb-4">
        <select
          onChange={handleBuildingChange}
          className="px-3 py-2 w-64 border border-gray-300 rounded-md shadow-sm text-sm text-gray-700 
    focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
        >
          {buildings.map((b) => (
            <option key={b.buildingId} value={b.buildingId}>
              {b.buildingName}
            </option>
          ))}
        </select>
      </div>

      {/* 강의실 Grid */}
      <div className="grid grid-cols-5 gap-6 justify-items-center">
        {paginatedRooms.map((room) => {
          const isOccupied = occupiedRoomIds.includes(room.roomId);
          const isDisabled = room.status !== "AVAILABLE";

          const cardStyle = isDisabled
            ? "bg-gray-400 text-white border-gray-500 cursor-not-allowed"
            : isOccupied
            ? "bg-blue-200 text-blue-900 border-blue-300 hover:bg-blue-300"
            : "bg-white text-gray-900 border-gray-300 hover:bg-blue-100";

          return (
            <div
              key={room.roomId}
              onClick={() =>
                !isDisabled && handleRoomClick(room.roomId, room.roomName)
              }
              className={`w-24 h-24 flex items-center justify-center rounded-md transition duration-200 shadow-sm border font-medium text-sm ${cardStyle}`}
            >
              {room.roomName}
            </div>
          );
        })}
      </div>

      <PageComponent
        currentPage={currentPage}
        totalPage={Math.ceil(rooms.length / roomsPerPage)}
        onPageChange={(page) => setCurrentPage(page)}
      />

      {/* 사용 현황 모달 */}
      <BaseModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <h2 className="text-xl font-semibold mb-4">
          {selectedBuildingName} {selectedRoomName} 강의실
        </h2>

        {selectedRoomUsage.length === 0 ? (
          <p className="text-gray-500 text-sm">현재 수업이 없습니다.</p>
        ) : (
          <ul className="text-base text-gray-800 leading-relaxed space-y-4">
            {selectedRoomUsage.map((cls) => (
              <li
                key={cls.classId}
                className="py-4 px-2 bg-gray-50 rounded-md shadow-sm space-y-1"
              >
                <div>
                  <span className="font-semibold text-gray-700">과목:</span>{" "}
                  {cls.courseName}
                </div>
                <div>
                  <span className="font-semibold text-gray-700">시간:</span>{" "}
                  {getPeriodRangeText(cls.startTime, cls.endTime)}
                </div>
                <div>
                  <span className="font-semibold text-gray-700">요일:</span>{" "}
                  {cls.day}요일
                </div>
                <div>
                  <span className="font-semibold text-gray-700">담당:</span>{" "}
                  {cls.professorName} 교수
                </div>
                <div>
                  <span className="font-semibold text-gray-700">소속:</span>{" "}
                  {cls.departmentName}
                </div>
              </li>
            ))}
          </ul>
        )}
        <button
          onClick={handleEditClick} // 수정 페이지로 이동
          className="mt-4 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
        >
          수정
        </button>
      </BaseModal>

      {/* 강의실 수정 모달 */}
      <BaseModal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)}>
        <AdminLectureRoomEditPage
          roomId={selectedRoomName} // 현재 선택된 강의실 ID를 전달
          onSuccess={() => {
            setIsEditModalOpen(false); // 수정 완료 후 모달 닫기
            handleBuildingChange({ target: { value: selectedBuildingId } }); // 건물 변경
          }}
        />
      </BaseModal>

      {/* 강의실 생성 모달 */}
      <BaseModal isOpen={isCreateModalOpen} onClose={() => setIsCreateModalOpen(false)}>
        <AdminLectureRoomCreatePage onSuccess={() => handleBuildingChange({ target: { value: selectedBuildingId } })} />
      </BaseModal>
    </div>
  );
};

export default AdminLectureRoomPage;