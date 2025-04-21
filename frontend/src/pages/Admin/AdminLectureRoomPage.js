import React, { useState, useEffect } from "react";
import {
  getAllBuildings,
  getLectureRoomsByBuilding,
  getLectureRoomUsage,
  deleteLectureRoom,
} from "../../api/adminLectureRoomApi";
import PageComponent from "../../components/PageComponent";
import BaseModal from "../../components/BaseModal";
import AdminLectureRoomCreatePage from "./AdminLectureRoomCreatePage";
import AdminLectureRoomEditPage from "./AdminLectureRoomEditPage";
import useConfirmModal from "../../hooks/useConfirmModal";
import { useDispatch } from "react-redux";
import { showModal } from "../../slices/modalSlice";

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
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedRoomId, setSelectedRoomId] = useState(null);

  const { openConfirm, ConfirmModalComponent } = useConfirmModal();
  const dispatch = useDispatch();

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
    const startTime = periodTimes[start - 1]?.time?.split(" ~ ")[0] || "";
    const endTime = periodTimes[end - 1]?.time?.split(" ~ ")[1] || "";
    return `${start}교시 ~ ${end}교시 (${startTime} ~ ${endTime})`;
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
          handleBuildingChange({
            target: { value: buildingList[0].buildingId },
          });
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
      setSelectedRoomId(roomId);
      setSelectedBuildingName(buildingName);
      setIsModalOpen(true);
    } catch {
      setSelectedRoomUsage([]);
      setSelectedBuildingName(buildingName);
    }
  };

  const handleEditClick = () => {
    setIsEditModalOpen(true);
  };

  const handleDeleteRoom = () => {
    openConfirm(
      `${selectedBuildingName} ${selectedRoomName}\n\n이 강의실을 삭제하시겠습니까?`,
      async () => {
        try {
          await deleteLectureRoom(selectedRoomId);
          dispatch(showModal("강의실이 성공적으로 삭제되었습니다."));
          setIsModalOpen(false);
          setSelectedRoomName("");
          setSelectedRoomId(null);
          handleBuildingChange({ target: { value: selectedBuildingId } });
        } catch (err) {
          dispatch(
            showModal({
              message:
                err?.response?.data || "강의실 삭제 중 오류가 발생했습니다.",
              type: "error",
            })
          );
        }
      }
    );
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">
          강의실 사용 현황
        </h2>
        <button
          onClick={() => setIsCreateModalOpen(true)}
          className="px-4 py-2 bg-blue-700 text-white rounded hover:bg-blue-800 transition"
        >
          강의실 등록
        </button>
      </div>

      <div className="mb-4">
        <select
          onChange={handleBuildingChange}
          className="px-3 py-2 w-64 border border-gray-300 rounded-md shadow-sm text-sm"
        >
          {buildings.map((b) => (
            <option key={b.buildingId} value={b.buildingId}>
              {b.buildingName}
            </option>
          ))}
        </select>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4 justify-items-center mt-10 mb-10">
        {paginatedRooms.map((room) => {
          const isOccupied = occupiedRoomIds.includes(room.roomId);
          const cardStyle =
            room.status !== "AVAILABLE"
              ? "bg-gray-400 text-white border-gray-300 hover:bg-gray-500"
              : isOccupied
              ? "bg-blue-200 text-blue-900 border-blue-300 hover:bg-blue-300"
              : "bg-white text-gray-900 border-gray-300 hover:bg-gray-100";

          return (
            <div
              key={room.roomId}
              onClick={() => handleRoomClick(room.roomId, room.roomName)}
              className={`w-full max-w-[96px] h-24 flex items-center justify-center rounded-md transition duration-200 shadow-sm border font-medium text-sm ${cardStyle}`}
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
      <BaseModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center">
            <h2 className="text-xl font-semibold">
              {selectedBuildingName} {selectedRoomName} 강의실
            </h2>
            <div className="flex gap-2 ml-4">
              <button
                onClick={handleEditClick}
                className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 text-base font-medium"
              >
                수정
              </button>
              <button
                onClick={handleDeleteRoom}
                className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 text-base font-medium"
              >
                삭제
              </button>
            </div>
          </div>
        </div>

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
                  <span className="font-semibold">과목:</span> {cls.courseName}
                </div>
                <div>
                  <span className="font-semibold">시간:</span>{" "}
                  {getPeriodRangeText(cls.startTime, cls.endTime)}
                </div>
                <div>
                  <span className="font-semibold">요일:</span> {cls.day}요일
                </div>
                <div>
                  <span className="font-semibold">담당:</span>{" "}
                  {cls.professorName} 교수
                </div>
                <div>
                  <span className="font-semibold">소속:</span>{" "}
                  {cls.departmentName}
                </div>
              </li>
            ))}
          </ul>
        )}
      </BaseModal>

      <BaseModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
      >
        <AdminLectureRoomEditPage
          roomId={selectedRoomId}
          rooms={rooms}
          onSuccess={(newName) => {
            setIsEditModalOpen(false);
            setIsModalOpen(false);
            handleBuildingChange({ target: { value: selectedBuildingId } });
            setSelectedRoomName(newName);
          }}
        />
      </BaseModal>

      <BaseModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      >
        <AdminLectureRoomCreatePage
          onSuccess={() => {
            setIsCreateModalOpen(false);
            handleBuildingChange({ target: { value: selectedBuildingId } });
          }}
        />
      </BaseModal>

      {ConfirmModalComponent}
    </div>
  );
};

export default AdminLectureRoomPage;
