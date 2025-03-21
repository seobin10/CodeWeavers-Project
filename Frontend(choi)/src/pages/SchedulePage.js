import React, { useEffect, useState } from "react";
import axios from "axios";

const days = ["월", "화", "수", "목", "금"];
const periods = Array.from({ length: 12 }, (_, i) => `${9 + i}시`);

const SchedulePage = () => {
  const [schedule, setSchedule] = useState([]);

  useEffect(() => {
    fetchSchedule();
  }, []);

  const fetchSchedule = () => {
    axios
      .get("http://localhost:8080/api/enrollment")
      .then((response) => {
        setSchedule(response.data);
      })
      .catch((error) => {
        console.error("시간표 조회 실패:", error);
      });
  };

  const handleUnenroll = async (enrollmentId) => {
    try {
      await axios.delete(
        `http://localhost:8080/api/enrollment/${enrollmentId}`
      );
      alert("수강이 취소되었습니다.");
      fetchSchedule(); // 수강 취소 후 즉시 시간표 업데이트
    } catch (error) {
      console.error("수강 취소 실패:", error);
      alert("수강 취소에 실패했습니다.");
    }
  };

  return (
    <div className="max-w-5xl mx-auto p-6 bg-white shadow-md mt-4 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-4">내 시간표</h2>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100 text-center">
              <th className="border border-gray-300 p-2">시간</th>
              {days.map((day) => (
                <th key={day} className="border border-gray-300 p-2">
                  {day}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {periods.map((time, periodIndex) => (
              <tr key={time} className="text-center h-16">
                <td className="border border-gray-300 p-2 bg-gray-200">
                  {time}
                </td>
                {days.map((day) => {
                  const course = schedule.find(
                    (c) =>
                      c.classDay === day &&
                      c.classStartPeriod <= periodIndex + 9 &&
                      c.classEndPeriod > periodIndex + 9
                  );
                  return (
                    <td
                      key={`${day}-${time}`}
                      className="border border-gray-300 relative p-2"
                    >
                      {course ? (
                        <div className="bg-blue-200 p-2 rounded shadow-md text-sm">
                          <p className="font-bold">{course.courseName}</p>
                          <p>{course.professorName}</p>
                          <p>{course.classRoom}</p>
                          <button
                            onClick={() => handleUnenroll(course.enrollmentId)}
                            className="mt-1 bg-red-500 text-white px-2 py-1 text-xs rounded hover:bg-red-700"
                          >
                            삭제
                          </button>
                        </div>
                      ) : null}
                    </td>
                  );
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default SchedulePage;
