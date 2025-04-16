import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

const images = [
  "/images/Eon1.jpg",
  "/images/Eon3.jpg",
  "/images/Eon4.jpg",
  "/images/Eon6.jpg",
  "/images/Eon7.jpg",
];

const MainPage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [noticeList, setNoticeList] = useState([]);
  const [qnaList, setQnaList] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate();

  const calender = [
    { date: "2025-03-02", event: "개강" },
    { date: "2025-03-05", event: "수강신청 마감" },
    { date: "2025-03-15", event: "수업 취소 마감" },
  ];

  useEffect(() => {
    const fetchNotices = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/notice/list",
          getAuthHeader()
        );
        const sorted = res.data.sort(
          (a, b) => new Date(b.noticeDate) - new Date(a.noticeDate)
        );
        setNoticeList(sorted.slice(0, 6));
      } catch (err) {
        console.error("공지사항 불러오기 실패", err);
      }
    };
    fetchNotices();
  }, []);

  useEffect(() => {
    const fetchQnaList = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/user/qna/list",
          getAuthHeader()
        );
        setQnaList(res.data.slice(0, 3));
      } catch (err) {
        console.error("Q&A 불러오기 실패", err);
      }
    };
    fetchQnaList();
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const handleSearchKeyDown = (e) => {
    if (e.key === "Enter" && searchTerm.trim()) {
      navigate("/main/noticelist", {
        state: { keyword: searchTerm.trim() },
      });
    }
  };

  const handleTagClick = (tag) => {
    const keyword = tag.replace(/^#/, "");
    navigate("/main/noticelist", {
      state: { keyword },
    });
  };

  return (
    <div className="max-w-screen-2xl mx-auto mt-5 px-0">
      <div className="flex flex-col md:flex-row gap-20 items-stretch">
        <div className="flex flex-col space-y-10 w-full md:w-1/2">
          <h1 className="text-5xl md:text-7xl font-semibold leading-tight pb-12 space-y-1">
            <div className="text-left">𝐈𝐧𝐧𝐨𝐯𝐚𝐭𝐞.</div>
            <div className="text-left">𝐈𝐭𝐞𝐫𝐚𝐭𝐞.</div>
            <div className="text-left">𝐈𝐥𝐥𝐮𝐦𝐢𝐧𝐚𝐭𝐞</div>
            <div className="text-right mt-10 text-3xl">– 𝐀𝐭 𝐄.𝐎𝐍</div>
          </h1>

          <div>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyDown={handleSearchKeyDown}
              placeholder="검색어를 입력해주세요."
              className="w-full border-b-2 border-black px-3 py-4 text-lg focus:outline-none"
            />
            <div className="flex flex-wrap gap-2 mt-3 text-sm text-gray-600">
              {[
                "#휴학",
                "#복학",
                "#수강신청",
                "#성적조회",
                "#강의평가",
                "#중간고사",
                "#예비군",
              ].map((tag, idx) => (
                <span
                  key={idx}
                  className="bg-gray-100 px-2 py-1 rounded hover:bg-gray-300 cursor-pointer"
                  onClick={() => handleTagClick(tag)}
                >
                  {tag}
                </span>
              ))}
            </div>
          </div>

          <div className="flex-1 flex flex-col">
            <div className="flex justify-between items-center mb-6 mt-7">
              <h2 className="text-2xl font-semibold"> 📢 공지사항 </h2>
              <Link
                to="/main/noticelist"
                className="text-blue-500 text-sm hover:underline"
              >
                전체보기
              </Link>
            </div>
            <ul className="space-y-4 text-sm">
              {noticeList.map((n, idx) => (
                <li key={idx} className="flex justify-between text-gray-800">
                  <Link
                    to="/main/noticedata"
                    state={{ noticeId: n.noticeId }}
                    className="hover:underline"
                  >
                    {n.title}
                  </Link>
                  <span>{n.noticeDate?.slice(5, 10)}</span>
                </li>
              ))}
            </ul>
          </div>

          <div className="flex-1 flex flex-col">
            <div className="flex justify-between items-center mb-6 mt-5">
              <h2 className="text-2xl font-semibold"> 💬 Q&A 게시판 </h2>
              <Link
                to="/main/qnalist"
                className="text-blue-500 text-sm hover:underline"
              >
                전체보기
              </Link>
            </div>
            <ul className="space-y-4 text-sm text-gray-800">
              {qnaList.length > 0 ? (
                qnaList.map((qna, idx) => (
                  <li key={idx} className="flex justify-between">
                    <Link
                      to="/main/qnadata"
                      state={{ questionId: qna.questionId, page: 1 }}
                      className="hover:underline w-full truncate"
                    >
                      {qna.title.includes("🔒")
                        ? "🔒 비밀글입니다."
                        : qna.title}
                    </Link>
                    <span className="whitespace-nowrap pl-2 text-gray-500 text-xs">
                      {qna.createdAt?.slice(5, 10)}
                    </span>
                  </li>
                ))
              ) : (
                <li className="text-gray-500">Q&A 정보를 불러오는 중...</li>
              )}
            </ul>
          </div>
        </div>

        <div className="flex flex-col w-full md:w-1/2 flex-2 gap-10 mt-4">
          <div className="relative w-full h-[450px] overflow-hidden rounded-lg shadow-lg">
            {images.map((img, idx) => (
              <img
                key={idx}
                src={img}
                alt="홍보 이미지"
                className={`absolute top-0 left-0 w-full h-full object-cover transition-opacity duration-1000 ${
                  idx === currentIndex ? "opacity-100 z-10" : "opacity-0 z-0"
                }`}
              />
            ))}
          </div>

          <div className="flex flex-col space-y-8">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-semibold">📅 학사일정</h2>
              <Link
                to="/main/calender"
                className="text-sm text-blue-500 hover:underline"
              >
                전체보기
              </Link>
            </div>
            <div className="grid grid-cols-7 gap-1 text-xs text-center border p-6 rounded">
              {["일", "월", "화", "수", "목", "금", "토"].map((d, idx) => (
                <div key={idx} className="font-bold text-gray-700">
                  {d}
                </div>
              ))}
              {Array.from({ length: 31 }).map((_, day) => {
                const dateStr = `2025-03-${String(day + 1).padStart(2, "0")}`;
                const match = calender.find((c) => c.date === dateStr);
                return (
                  <div
                    key={day}
                    className={`h-16 border text-[10px] p-2 ${
                      match ? "bg-yellow-100 font-bold" : ""
                    }`}
                  >
                    <div>{day + 1}</div>
                    {match && <div>{match.event}</div>}
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
