import React, { useEffect, useState, useContext } from "react";
import { getAllUsers, deleteUser } from "../../api/adminUserApi";
import { ModalContext } from "../../App";
import PageComponent from "../../components/PageComponent";
import AdminUserModal from "../../components/AdminUserModal";
import AdminUserCreatePage from "./AdminUserCreatePage";
import AdminUserEditPage from "./AdminUserEditPage";

const AdminUserListPage = () => {
  const [users, setUsers] = useState({
    dtoList: [],
    totalPage: 0,
    current: 1,
    totalCount: 0,
  });
  const [currentPage, setCurrentPage] = useState(1);
  const [searchQuery, setSearchQuery] = useState("");
  const [sortField, setSortField] = useState("userId");
  const [sortDir, setsortDir] = useState("asc");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { showModal, showConfirm } = useContext(ModalContext);
  const [hoveredUser, setHoveredUser] = useState(null);
  const [mousePos, setMousePos] = useState({ x: 0, y: 0 });
  const [editUser, setEditUser] = useState(null);

  const fetchUsers = async (page = 1) => {
    try {
      const safeSortField = sortField || "userId";
      const res = await getAllUsers(
        page,
        10,
        searchQuery,
        safeSortField,
        sortDir
      );
      setUsers(res.data);
      setCurrentPage(page);
    } catch (err) {
      showModal("사용자 목록을 불러오지 못했습니다.");
    }
  };

  useEffect(() => {
    fetchUsers(1);
  }, [searchQuery, sortField, sortDir]);

  const handleDelete = (user) => {
    showConfirm({
      message: (
        <>
          ID: {user.userId}
          <br />
          이름: {user.userName}
          <br />
          <br />이 사용자를 삭제하시겠습니까?
        </>
      ),
      onConfirm: async () => {
        try {
          await deleteUser(user.userId);
          showModal("사용자가 성공적으로 삭제되었습니다.");
          fetchUsers(currentPage);
        } catch (err) {
          showModal("사용자 삭제 중 오류가 발생했습니다.");
        }
      },
    });
  };

  const handleSort = (field) => {
    if (sortField === field) {
      setsortDir((prev) => (prev === "asc" ? "desc" : "asc"));
    } else {
      setSortField(field);
      setsortDir("asc");
    }
  };

  const getSortIcon = () => "↕️";

  const getRoleLabel = (role) => {
    switch (role) {
      case "STUDENT":
        return "학생";
      case "PROFESSOR":
        return "교수";
      case "ADMIN":
        return "관리자";
      default:
        return role;
    }
  };

  const handleMouseMove = (e) => {
    setMousePos({ x: e.clientX, y: e.clientY });
  };

  return (
    <div className="max-w-6xl mx-auto p-6 bg-white shadow-lg mt-10 rounded-xl">
      {/* 상단 영역 */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-gray-800">📋 사용자 목록</h2>
        <button
          onClick={() => setIsModalOpen(true)}
          className="bg-blue-600 hover:bg-blue-800 text-white px-4 py-2 rounded-md shadow transition"
        >
          + 사용자 등록
        </button>
      </div>

      {/* 검색창 */}
      <div className="mb-6 flex justify-center">
        <input
          type="text"
          placeholder="이름 또는 ID 검색"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="border border-gray-300 px-4 py-2 w-1/2 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
      </div>

      {/* 사용자 목록 테이블 */}
      {users.dtoList.length > 0 ? (
        <>
          <table className="w-full text-sm border border-gray-300 rounded-md overflow-hidden shadow">
            <thead className="bg-gray-100 text-center text-gray-700">
              <tr>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userId")}
                >
                  ID {getSortIcon()}
                </th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userName")}
                >
                  이름 {getSortIcon()}
                </th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userBirth")}
                >
                  생년월일 {getSortIcon()}
                </th>
                <th className="border p-2">이메일</th>
                <th className="border p-2">전화번호</th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userRole")}
                >
                  구분 {getSortIcon()}
                </th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("departmentName")}
                >
                  학과 {getSortIcon()}
                </th>
                <th className="border p-2">변경</th>
              </tr>
            </thead>
            <tbody>
              {users.dtoList.map((user) => (
                <tr
                  key={user.userId}
                  className="text-center hover:bg-gray-50 transition"
                  onMouseEnter={() => setHoveredUser(user)}
                  onMouseLeave={() => setHoveredUser(null)}
                  onMouseMove={handleMouseMove}
                >
                  <td className="border p-2">{user.userId}</td>
                  <td className="border p-2">{user.userName}</td>
                  <td className="border p-2">{user.userBirth}</td>
                  <td className="border p-2">{user.userEmail}</td>
                  <td className="border p-2">{user.userPhone}</td>
                  <td className="border p-2">{getRoleLabel(user.userRole)}</td>
                  <td className="border p-2">
                    {user.departmentName || "없음"}
                  </td>
                  <td className="border p-2">
                    <div className="flex justify-center space-x-2">
                      <button
                        onClick={() => setEditUser(user)}
                        className="bg-green-400 hover:bg-green-500 text-white px-3 py-1 rounded-md text-xs shadow-sm transition"
                      >
                        수정
                      </button>
                      <button
                        onClick={() => handleDelete(user)}
                        className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded-md text-xs shadow-sm transition"
                      >
                        삭제
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <PageComponent
            currentPage={users.current}
            totalPage={users.totalPage}
            onPageChange={(page) => fetchUsers(page)}
          />
        </>
      ) : (
        <p className="text-center text-gray-500 mt-10">
          등록된 사용자가 없습니다.
        </p>
      )}

      {/* 사용자 등록 모달 */}
      <AdminUserModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      >
        <AdminUserCreatePage
          onSuccess={() => {
            fetchUsers(currentPage); // 새로 고침
          }}
        />
      </AdminUserModal>

      {/* 사용자 수정 모달 */}
      <AdminUserModal isOpen={!!editUser} onClose={() => setEditUser(null)}>
        <AdminUserEditPage
          user={editUser}
          onSuccess={() => {
            fetchUsers(currentPage);
            setEditUser(null);
          }}
          onClose={() => setEditUser(null)}
        />
      </AdminUserModal>

      {hoveredUser && (
        <div
          className="fixed z-50 pointer-events-none"
          style={{
            top: mousePos.y + 10 + "px",
            left: mousePos.x + 10 + "px",
          }}
        >
          <div className="bg-white border shadow-md rounded p-1 w-24">
            <img
              src={
                hoveredUser.userImgUrl
                  ? `http://localhost:8080${hoveredUser.userImgUrl}`
                  : "/images/noImage.jpg"
              }
              alt="프로필"
              className="w-full h-auto object-cover rounded"
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminUserListPage;
