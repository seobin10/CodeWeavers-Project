import React, { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import axios from "axios";
import PageComponent from "../components/PageComponent";
import { getAuthHeader } from "../util/authHeader";
import { useSelector } from "react-redux";

const NoticeListPage = () => {
  const [message, setMessage] = useState("");
  const [noticeInfo, setNoticeInfo] = useState([]);
  const location = useLocation();
  const checkPage = location.state?.page ?? 1;
  const [currentPage, setCurrentPage] = useState(checkPage);
  const itemCount = 10;

  const userId = useSelector((state) => state.auth?.userId);
  const userRole = useSelector((state) => state.auth?.userRole);

  useEffect(() => {
    if (userId) {
      fetchNoticeInfo();
    }
  }, [userId]);

  const fetchNoticeInfo = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/notice/list",
        getAuthHeader()
      );
      setNoticeInfo(response.data);
    } catch (error) {
      setMessage("공지 정보를 불러올 수 없습니다.");
    }
  };

  const handlePage = (page) => {
    setCurrentPage(page);
  };
  
  // 고정 처리
  const pinned = noticeInfo.filter((i) => i.pin === 1);
  const unpinned = noticeInfo.filter((i) => i.pin !== 1);

  const lastItem = currentPage * itemCount;
  const firstItem = lastItem - itemCount;
  const currentItem = unpinned.slice(firstItem, lastItem);
  const totalPage = Math.ceil(unpinned.length / itemCount);

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <h1 className="text-3xl font-bold text-center mb-6">공지사항</h1>
      {message && <p className="text-red-500 text-center">{message}</p>}
      <hr />
      <br />
      <div>
        <table className="table-auto border-collapse w-full">
          <thead className="bg-blue-800 text-white">
            <tr>
              <th className="border border-blue-800 border-x-0 px-4 py-2">
                번호
              </th>
              <th className="border border-blue-800 border-x-0 px-4 py-2">
                제목
              </th>
              <th className="border border-blue-800 border-x-0 px-4 py-2">
                작성자
              </th>
              <th className="border border-blue-800 border-x-0 px-4 py-2">
                작성일
              </th>
              <th className="border border-blue-800 border-x-0 px-4 py-2">
                조회수
              </th>
            </tr>
          </thead>
          <tbody>
            {currentItem.length > 0 ? (
              [...pinned, ...currentItem].map((notice, i) => {
                const cellStyle =
                  notice.pin === 1
                    ? "text-center hover:bg-blue-100 bg-blue-50"
                    : "text-center hover:bg-gray-100";

                return (
                  <tr key={i} className={cellStyle}>
                    <td className="border border-gray-400 border-x-0 px-4 py-2">
                      {notice.pin === 1
                        ? "\u{1F4CC}"
                        : firstItem + i + 1 - pinned.length}
                    </td>
                    <td
                      className={
                        notice.pin === 1
                          ? `font-bold text-left border border-gray-400 border-x-0 px-4 py-2`
                          : `text-left border border-gray-400 border-x-0 px-4 py-2`
                      }
                    >
                      <Link
                        to="/main/noticedata"
                        state={{
                          noticeId: notice.noticeId,
                          page: currentPage,
                        }}
                      >
                        {notice.title}
                      </Link>
                    </td>
                    <td className="border border-gray-400 border-x-0 px-4 py-2">
                      관리자({notice.adminId})
                    </td>
                    <td className="border border-gray-400 border-x-0 px-4 py-2">
                      {notice.noticeDate}
                    </td>
                    <td className="border border-gray-400 border-x-0 px-4 py-2">
                      {notice.viewCount}
                    </td>
                  </tr>
                );
              })
            ) : (
              <tr>
                <td colSpan="6" className="text-center text-gray-500 py-4">
                  공지 정보를 불러오는 중...
                </td>
              </tr>
            )}
          </tbody>
        </table>
        <PageComponent
          currentPage={currentPage}
          totalPage={totalPage}
          onPageChange={handlePage}
        />
      </div>
      <br />
      {userRole === "ADMIN" && (
        <div className="flex justify-end mt-4">
          <Link
            to="/main/noticewrite"
            className="bg-blue-500 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-3 rounded transition"
          >
            &nbsp;등록&nbsp;
          </Link>
        </div>
      )}
    </div>
  );
};

export default NoticeListPage;
