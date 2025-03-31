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
  const [sortDir, setSortDir] = useState("asc");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { showModal, showConfirm } = useContext(ModalContext);
  const [editUser, setEditUser] = useState(null);
  const [hoveredUser, setHoveredUser] = useState(null);

  const fetchUsers = async (page = 1) => {
    try {
      const res = await getAllUsers(page, 10, searchQuery, sortField, sortDir);
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
      message: `ID: ${user.userId}\n이름: ${user.userName}\n\n이 사용자를 삭제하시겠습니까?`,
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
    setSortDir(sortField === field && sortDir === "asc" ? "desc" : "asc");
    setSortField(field);
  };

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

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <div className="flex justify-between items-center border-b pb-3 mb-6">
        <h2 className="text-2xl font-semibold text-gray-700">사용자 관리</h2>
        <button
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2 bg-blue-700 text-white rounded hover:bg-blue-800 transition"
        >
          사용자 추가
        </button>
      </div>

      <div className="flex justify-end mb-4">
        <input
          type="text"
          placeholder="ID 또는 이름으로 검색"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="border-gray-300 rounded px-3 py-2 shadow-sm w-64 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <table className="min-w-full table-auto shadow-sm border border-gray-200 rounded-md">
        <thead className="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
          <tr>
            {[
              { label: "학번/교번", field: "userId" },
              { label: "이름", field: "userName" },
              { label: "생년월일", field: "userBirth" },
              { label: "이메일" },
              { label: "전화번호" },
              { label: "구분", field: "userRole" },
              { label: "학과", field: "departmentName" },
              { label: "관리" },
            ].map(({ label, field }) => (
              <th
                key={label}
                className="py-3 px-4 cursor-pointer border-b"
                onClick={() => field && handleSort(field)}
              >
                {label}
                {field &&
                  (sortField === field
                    ? sortDir === "asc"
                      ? " ▲"
                      : " ▼"
                    : " ▲")}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="text-gray-700 text-sm">
          {users.dtoList.map((user) => (
            <tr
              key={user.userId}
              className="hover:bg-gray-100 border-b text-center"
            >
              <td className="py-3 px-4">{user.userId}</td>
              <td
                className="py-3 px-4 text-center relative"
                onMouseEnter={() => setHoveredUser(user)}
                onMouseLeave={() => setHoveredUser(null)}
              >
                {user.userName}

                {hoveredUser?.userId === user.userId && (
                  <div className="absolute bottom-full mb-2 left-1/2 transform -translate-x-1/2 w-24 h-24 bg-white border rounded shadow z-10">
                    <img
                      src={
                        hoveredUser.userImgUrl
                          ? `http://localhost:8080${hoveredUser.userImgUrl}`
                          : "/images/noImage.jpg"
                      }
                      alt="프로필 이미지"
                      className="w-full h-full object-cover rounded"
                    />
                  </div>
                )}
              </td>
              <td className="py-3 px-4">{user.userBirth}</td>
              <td className="py-3 px-4">{user.userEmail}</td>
              <td className="py-3 px-4">{user.userPhone}</td>
              <td className="py-3 px-4">{getRoleLabel(user.userRole)}</td>
              <td className="py-3 px-4">{user.departmentName || "-"}</td>
              <td className="py-3 px-4 space-x-2">
                <button
                  onClick={() => setEditUser(user)}
                  className="text-white bg-green-600 px-2 py-1 rounded hover:bg-green-700 transition"
                >
                  수정
                </button>
                <button
                  onClick={() => handleDelete(user)}
                  className="text-white bg-red-600 px-2 py-1 rounded hover:bg-red-700 transition"
                >
                  삭제
                </button>
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

      <AdminUserModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      >
        <AdminUserCreatePage onSuccess={() => fetchUsers(currentPage)} />
      </AdminUserModal>

      <AdminUserModal isOpen={!!editUser} onClose={() => setEditUser(null)}>
        <AdminUserEditPage
          user={editUser}
          onSuccess={() => fetchUsers(currentPage)}
          onClose={() => setEditUser(null)}
        />
      </AdminUserModal>
    </div>
  );
};

export default AdminUserListPage;
