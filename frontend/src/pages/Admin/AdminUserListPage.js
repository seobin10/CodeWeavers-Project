import React, { useEffect, useState, useContext } from "react";
import { getAllUsers, deleteUser } from "../../api/adminUserApi";
import { ModalContext } from "../../App";
import PageComponent from "../../components/PageComponent";

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
  const { showModal, showConfirm } = useContext(ModalContext);

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
      // 같은 컬럼이면 방향만 토글
      setsortDir((prev) => (prev === "asc" ? "desc" : "asc"));
    } else {
      // 새로운 컬럼이면 초기 asc로 설정
      setSortField(field);
      setsortDir("asc");
    }
  };

  const getSortIcon = (field) => {
    if (sortField !== field) return "↕️";
    return sortDir === "asc" ? "⬆️" : "⬇️";
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
    <div className="max-w-6xl mx-auto p-4 bg-white shadow-md mt-6 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-4">사용자 목록</h2>

      <div className="mb-4 text-center">
        <input
          type="text"
          placeholder="이름 또는 ID 검색"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="border p-2 rounded w-1/2"
        />
      </div>

      {users.dtoList.length > 0 ? (
        <>
          <table className="w-full border border-gray-300 text-sm">
            <thead>
              <tr className="bg-gray-100 text-center">
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userId")}
                >
                  ID {getSortIcon("userId")}
                </th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userName")}
                >
                  이름 {getSortIcon("userName")}
                </th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userBirth")}
                >
                  생년월일 {getSortIcon("userBirth")}
                </th>
                <th className="border p-2">이메일</th>
                <th className="border p-2">전화번호</th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("userRole")}
                >
                  구분 {getSortIcon("userRole")}
                </th>
                <th
                  className="border p-2 cursor-pointer"
                  onClick={() => handleSort("departmentName")}
                >
                  학과 {getSortIcon("departmentName")}
                </th>
                <th className="border p-2">삭제</th>
              </tr>
            </thead>
            <tbody>
              {users.dtoList.map((user) => (
                <tr key={user.userId} className="text-center">
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
                    <button
                      onClick={() => handleDelete(user)}
                      className="bg-red-500 hover:bg-red-700 text-white px-3 py-1 rounded text-xs"
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
        </>
      ) : (
        <p className="text-center text-gray-500 mt-10">
          등록된 사용자가 없습니다.
        </p>
      )}
    </div>
  );
};

export default AdminUserListPage;
