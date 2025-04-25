// 전체 MainPage.js 전체 코드 (수정 포함)
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { getAuthHeader } from "../util/authHeader";
import { getEnrolledCourses } from "../api/enrollmentApi";
import { useSelector } from "react-redux";

const images = [
  "/images/Eon1.jpg",
  "/images/Eon2.jpg",
  "/images/Eon3.jpg",
  "/images/Eon4.jpg",
  "/images/Eon5.jpg",
  "/images/Eon6.jpg",
];

const imageNames = [
  "컴퓨터정보공학관",
  "AI융합관",
  "데이터사이언스관",
  "임베디드·IoT관",
  "정보보호관",
  "IT융합실습관",
];

const bgColors = [
  "bg-red-100",
  "bg-blue-100",
  "bg-green-100",
  "bg-yellow-100",
  "bg-purple-100",
  "bg-pink-100",
  "bg-orange-100",
  "bg-teal-100",
];

const periods = [
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

const days = ["월", "화", "수", "목", "금"];

const MainPage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [noticeList, setNoticeList] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [schedule, setSchedule] = useState([]);
  const [calendarEvents, setCalendarEvents] = useState([]);
  const navigate = useNavigate();
  const { userRole, userId } = useSelector((state) => state.auth);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prev) => (prev + 1) % images.length);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

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
        setNoticeList(sorted.slice(0, 5));
      } catch (err) {
        console.error("공지사항 불러오기 실패", err);
      }
    };
    fetchNotices();
  }, []);

  useEffect(() => {
    const fetchSchedule = async () => {
      try {
        const res = await getEnrolledCourses(userId);
        setSchedule(res.data);
      } catch (e) {
        console.error("시간표 불러오기 실패", e);
      }
    };
    if (userRole === "STUDENT" && userId) {
      fetchSchedule();
    }
  }, [userId, userRole]);

  useEffect(() => {
    const fetchCalendarEvents = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/calendar",
          getAuthHeader()
        );
        const eventList = res.data.map((e) => ({
          ...e,
          color: bgColors[Math.floor(Math.random() * bgColors.length)],
        }));
        setCalendarEvents(eventList);
      } catch (e) {
        console.error("학사일정 불러오기 실패", e);
      }
    };
    fetchCalendarEvents();
  }, []);

  const maxPeriod =
    schedule.length > 0
      ? Math.max(...schedule.map((c) => c.classEndPeriod))
      : 10;

  const isEventDay = (dateStr) =>
    calendarEvents.some((e) => dateStr >= e.start && dateStr <= e.end);
  const getEventForDay = (dateStr) =>
    calendarEvents.find((e) => dateStr >= e.start && dateStr <= e.end);

  const handleSearchKeyDown = (e) => {
    if (e.key === "Enter" && searchTerm.trim()) {
      navigate("/main/noticelist", { state: { keyword: searchTerm.trim() } });
    }
  };

  const handleTagClick = (tag) => {
    const keyword = tag.replace(/^#/, "");
    navigate("/main/noticelist", { state: { keyword } });
  };

  return (
    <div className="max-w-screen-2xl mx-auto mt-6 px-4">
      <div className="flex flex-col md:flex-row gap-10 items-start">
        <div className="w-full md:w-1/2 flex flex-col space-y-10 pl-6 pr-6">
          {userRole === "STUDENT" && (
            <div className="relative w-full h-[400px] overflow-hidden rounded-lg shadow-lg">
              {images.map((img, idx) => (
                <div
                  key={idx}
                  className={`absolute top-0 left-0 w-full h-full transition-opacity duration-1000 ${
                    idx === currentIndex ? "opacity-100 z-10" : "opacity-0 z-0"
                  }`}
                >
                  <img
                    src={img}
                    alt={`홍보 이미지 ${idx + 1}`}
                    className="w-full h-full object-cover"
                  />
                  <div className="absolute inset-0 bg-black bg-opacity-30">
                    <span className="absolute bottom-2 right-4 text-gray-200 text-lg font-bold drop-shadow-md">
                      {imageNames[idx]}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}

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

          <div className="flex flex-col">
            <div className="flex justify-between items-center mb-4 mt-4">
              <h2 className="text-2xl font-semibold">📢 공지사항</h2>
              <Link
                to="/main/noticelist"
                className="text-blue-500 text-sm hover:underline"
              >
                전체보기
              </Link>
            </div>
            <ul className="space-y-3 text-sm">
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
        </div>

        <div className="w-full md:w-1/2 flex flex-col gap-6">
          {userRole === "STUDENT" && (
            <Link
              to="/main/schedule"
              className="bg-white shadow-md rounded-md p-4 hover:bg-gray-50 transition"
            >
              <div className="text-lg font-semibold mb-4">📘 나의 시간표</div>
              <div className="overflow-x-auto">
                <table className="w-full border-collapse border border-gray-300 text-xs table-fixed">
                  <thead>
                    <tr className="bg-gray-100 text-center">
                      <th className="border border-gray-300 px-1 w-[72px]">
                        시간
                      </th>
                      {days.map((day) => (
                        <th
                          key={day}
                          className="border border-gray-300 p-2 w-1/5"
                        >
                          {day}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {periods.slice(0, maxPeriod).map((period, periodIndex) => {
                      const currentPeriod = periodIndex + 1;
                      const renderMap = {};
                      return (
                        <tr
                          key={period.label}
                          className="text-center h-[3.2rem]"
                        >
                          <td className="border border-gray-300 p-1 bg-gray-100">
                            <div className="font-semibold text-[11px]">
                              {period.label}
                            </div>
                            <div className="text-[9px] text-gray-500">
                              {period.time}
                            </div>
                          </td>
                          {days.map((day) => {
                            if (renderMap[`${day}-${currentPeriod}`])
                              return null;
                            const course = schedule.find(
                              (c) =>
                                c.classDay === day &&
                                c.classStartPeriod === currentPeriod
                            );
                            if (course) {
                              const duration =
                                course.classEndPeriod -
                                course.classStartPeriod +
                                1;
                              for (let i = 0; i < duration; i++) {
                                renderMap[`${day}-${currentPeriod + i}`] = true;
                              }
                              return (
                                <td
                                  key={`${day}-${period.label}`}
                                  rowSpan={duration}
                                  className="border border-gray-300 p-9 align-top bg-blue-100 text-[11px] font-semibold whitespace-nowrap overflow-hidden text-ellipsis"
                                >
                                  <div>{course.courseName}</div>
                                  <div>{course.classRoom}</div>
                                </td>
                              );
                            } else {
                              return (
                                <td
                                  key={`${day}-${period.label}`}
                                  className="border border-gray-300"
                                ></td>
                              );
                            }
                          })}
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </Link>
          )}

          <div className="bg-white shadow-md rounded-md p-4">
            <div className="flex justify-between items-center mb-2">
              <h2 className="text-lg font-semibold">📅 학사일정</h2>
              <Link
                to="/main/calender"
                className="text-sm text-blue-500 hover:underline"
              >
                전체보기
              </Link>
            </div>
            <div className="grid grid-cols-7 gap-1 text-xs text-center border p-2 rounded">
              {["일", "월", "화", "수", "목", "금", "토"].map((d, idx) => (
                <div key={idx} className="font-bold text-gray-700">
                  {d}
                </div>
              ))}
              {Array.from({ length: 31 }).map((_, day) => {
                const dateStr = `2025-04-${String(day + 1).padStart(2, "0")}`;
                const match = getEventForDay(dateStr);
                return (
                  <div
                    key={day}
                    className={`h-16 border text-[10px] p-2 ${
                      match ? `${match.color} font-bold` : ""
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
