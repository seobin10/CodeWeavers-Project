import React, { useContext, useEffect, useState, useCallback } from "react";
import { getEnrolledCourses } from "../api/enrollmentApi";
import { AuthContext } from "../App";

// 요일
const days = ["월", "화", "수", "목", "금"];

// 교시 + 시간
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

// 고정 색상 리스트
const COLORS = [
  "bg-red-200",
  "bg-orange-200",
  "bg-yellow-200",
  "bg-green-200",
  "bg-teal-200",
  "bg-blue-200",
  "bg-indigo-200",
  "bg-purple-200",
  "bg-pink-200",
  "bg-gray-200",
  "bg-lime-200",
];

// 과목명 기반으로 색상 인덱스 반환 (해시)
const getColorClass = (courseName) => {
  let hash = 0;
  for (let i = 0; i < courseName.length; i++) {
    hash = courseName.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % COLORS.length;
  return COLORS[index];
  
};

const SchedulePage = () => {
  const { userId } = useContext(AuthContext);
  const [schedule, setSchedule] = useState([]);

  const fetchSchedule = useCallback(async () => {
    try {
      const response = await getEnrolledCourses(userId);
      setSchedule(response.data);
    } catch (error) {
      console.error("시간표 조회 실패:", error);
    }
  }, [userId]);

  useEffect(() => {
    if (userId) fetchSchedule();
  }, [userId, fetchSchedule]);

  const renderMap = {};

  return (
    <div className="max-w-6xl mx-auto p-6 bg-blue-50 bg-opacity-40 shadow-sm mt-11 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-4">📝 시간표 📝</h2>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse border border-gray-300 table-fixed">
          <thead>
            <tr className="bg-gray-100 text-center">
              <th className="border border-gray-300 p-2 w-[130px]">시간</th>
              {days.map((day) => (
                <th key={day} className="border border-gray-300 p-2 w-1/5">
                  {day}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {periods.map((period, periodIndex) => {
              const currentPeriod = periodIndex + 1;

              return (
                <tr key={period.label} className="text-center h-[4.5rem]">
                  {/* 왼쪽 시간 표시 */}
                  <td className="border border-gray-300 p-2 bg-gray-200 text-sm">
                    <div className="font-semibold">{period.label}</div>
                    <div className="text-xs text-gray-600">{period.time}</div>
                  </td>

                  {/* 요일별 셀 렌더링 */}
                  {days.map((day) => {
                    if (renderMap[`${day}-${currentPeriod}`]) return null;

                    const course = schedule.find(
                      (c) =>
                        c.classDay === day &&
                        c.classStartPeriod === currentPeriod
                    );

                    if (course) {
                      const duration =
                        course.classEndPeriod - course.classStartPeriod + 1;

                      for (let i = 0; i < duration; i++) {
                        renderMap[`${day}-${currentPeriod + i}`] = true;
                      }

                      const bgColorClass = getColorClass(course.courseName);

                      return (
                        <td
                          key={`${day}-${period.label}`}
                          rowSpan={duration}
                          className="border border-gray-300 p-2 align-top"
                        >
                          <div
                            className={`${bgColorClass} h-full flex flex-col justify-center items-center p-2 rounded shadow text-sm`}
                            style={{ height: `${duration * 4.5}rem` }}
                          >
                            <p className="font-bold">{course.courseName}</p>
                            <p>{course.professorName}</p>
                            <p>{course.classRoom}</p>
                          </div>
                        </td>
                      );
                    } else {
                      return (
                        <td
                          key={`${day}-${period.label}`}
                          className="border border-gray-300 p-2 min-h-[4.5rem]"
                        />
                      );
                    }
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default SchedulePage;