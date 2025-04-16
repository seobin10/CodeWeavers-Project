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
  const keyword = location.state?.keyword?.trim() ?? ""; // 🔍 검색어 받아오기
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

  // 🔒 공지 분리 및 필터링 적용
  const pinned = noticeInfo.filter((i) => i.pin === 1);
  const unpinned = noticeInfo.filter((i) => i.pin !== 1);
  const filteredUnpinned = keyword
    ? unpinned.filter((n) => n.title.includes(keyword))
    : unpinned;

  const lastItem = currentPage * itemCount;
  const firstItem = lastItem - itemCount;
  const currentItem = filteredUnpinned.slice(firstItem, lastItem);
  const totalPage = Math.ceil(filteredUnpinned.length / itemCount);

  return (
    <div className="max-w-7xl mx-auto p-8 bg-white shadow-md rounded-md mt-10">
      <h1 className="text-3xl font-bold text-center mb-6">공지사항</h1>
      {keyword && (
        <p className="text-center text-sm text-gray-500 mb-4">
          "<span className="font-semibold">{keyword}</span>" 에 대한 검색 결과
          🔍
        </p>
      )}
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
            {[...pinned, ...currentItem].map((notice, i) => {
              const cellStyle =
                notice.pin === 1
                  ? "text-center hover:bg-blue-100 bg-blue-50"
                  : "text-center hover:bg-gray-100";

              return (
                <tr key={i} className={cellStyle}>
                  <td className="border border-gray-400 border-x-0 px-4 py-2">
                    {notice.pin === 1
                      ? "📌"
                      : firstItem + i + 1 - pinned.length}
                  </td>
                  <td
                    className={
                      notice.pin === 1
                        ? "font-bold text-left border border-gray-400 border-x-0 px-4 py-2"
                        : "text-left border border-gray-400 border-x-0 px-4 py-2"
                    }
                  >
                    <Link
                      to="/main/noticedata"
                      state={{ noticeId: notice.noticeId, page: currentPage }}
                    >
                      {notice.title}
                    </Link>
                  </td>
                  <td className="border border-gray-400 border-x-0 px-4 py-2">
                    관리자
                  </td>
                  <td className="border border-gray-400 border-x-0 px-4 py-2">
                    {notice.noticeDate}
                  </td>
                  <td className="border border-gray-400 border-x-0 px-4 py-2">
                    {notice.viewCount}
                  </td>
                </tr>
              );
            })}
            {pinned.length + currentItem.length === 0 && (
              <tr>
                <td colSpan="5" className="text-center text-gray-500 py-4">
                  검색 결과가 없습니다.
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
