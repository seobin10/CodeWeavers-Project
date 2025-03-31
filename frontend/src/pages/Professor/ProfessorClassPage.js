import React, { useEffect, useState, useContext } from "react";
import { getMyClasses, deleteClass } from "../../api/professorClassApi";
import { AuthContext, ModalContext } from "../../App";
import PageComponent from "../../components/PageComponent";
import BaseModal from "../../components/BaseModal";
import ProfessorClassCreatePage from "./ProfessorClassCreatePage";
import ProfessorClassEditPage from "./ProfessorClassEditPage";

const ProfessorClassPage = () => {
  const { showModal, showConfirm } = useContext(ModalContext);
  const { userId, setUserId } = useContext(AuthContext);

  const [classes, setClasses] = useState({
    dtoList: [],
    totalPage: 0,
    current: 1,
    totalCount: 0,
  });
  const [currentPage, setCurrentPage] = useState(1);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editClass, setEditClass] = useState(null);

  useEffect(() => {
    const localId = localStorage.getItem("id");
    if (!userId && localId) {
      setUserId(localId);
    }
  }, [userId, setUserId]);

  const fetchClasses = async (page = 1) => {
    if (!userId) return;
    try {
      const res = await getMyClasses(userId, page, 10);
      setClasses(res.data);
      setCurrentPage(page);
    } catch (err) {
      showModal("강의 목록을 불러오지 못했습니다.");
    }
  };

  useEffect(() => {
    if (userId) fetchClasses(1);
  }, [userId]);

  const handleDelete = (classData) => {
    showConfirm({
      message: `과목: ${classData.courseName}\n학기: ${classData.semester}\n\n 삭제하시겠습니까?`,
      onConfirm: async () => {
        try {
          await deleteClass(classData.classId);
          showModal("강의가 성공적으로 삭제되었습니다.");
          fetchClasses(currentPage);
        } catch (err) {
          showModal("삭제 중 오류 발생");
        }
      },
    });
  };

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">내 강의 관리</h2>
        <button
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2 bg-blue-700 text-white rounded hover:bg-blue-800 transition"
        >
          강의 등록
        </button>
      </div>

      <table className="min-w-full table-auto shadow-sm border border-gray-200 rounded-md">
        <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
          <tr>
            <th className="py-3 px-4">과목명</th>
            <th className="py-3 px-4">학기</th>
            <th className="py-3 px-4">요일</th>
            <th className="py-3 px-4">시간</th>
            <th className="py-3 px-4">강의실</th>
            <th className="py-3 px-4">수강인원</th>
            <th className="py-3 px-4">관리</th>
          </tr>
        </thead>
        <tbody className="text-gray-700 text-sm">
          {classes.dtoList.map((c) => (
            <tr
              key={c.classId}
              className="hover:bg-gray-100 border-b text-center"
            >
              <td className="py-3 px-4">{c.courseName}</td>
              <td className="py-3 px-4">{c.semester}</td>
              <td className="py-3 px-4">{c.day}</td>
              <td className="py-3 px-4">{`${c.startTime} ~ ${c.endTime}교시`}</td>
              <td className="py-3 px-4">{`${c.buildingName} ${c.lectureRoomName}`}</td>
              <td className="py-3 px-4">{`${c.enrolled} / ${c.capacity}`}</td>
              <td className="py-3 px-4 space-x-2">
                <button
                  onClick={() => setEditClass(c)}
                  className="text-white bg-green-600 px-2 py-1 rounded hover:bg-green-700"
                >
                  수정
                </button>
                <button
                  onClick={() => handleDelete(c)}
                  className="text-white bg-red-600 px-2 py-1 rounded hover:bg-red-700"
                >
                  삭제
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <PageComponent
        currentPage={classes.current}
        totalPage={classes.totalPage}
        onPageChange={(page) => fetchClasses(page)}
      />

      <BaseModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <ProfessorClassCreatePage
          professorId={userId}
          onSuccess={() => fetchClasses(currentPage)}
        />
      </BaseModal>

      <BaseModal isOpen={!!editClass} onClose={() => setEditClass(null)}>
        <ProfessorClassEditPage
          classData={editClass}
          onSuccess={() => fetchClasses(currentPage)}
          onClose={() => setEditClass(null)}
        />
      </BaseModal>
    </div>
  );
};

export default ProfessorClassPage;
