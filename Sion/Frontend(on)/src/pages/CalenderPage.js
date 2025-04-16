import React from "react";

const CalenderPage = () => {
  const calender = [
    { date: "2025-03-02", event: "개강" },
    { date: "2025-03-05", event: "수강신청 마감" },
    { date: "2025-03-15", event: "수업 취소 마감" },
  ];

  return (
    <div className="max-w-3xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">📅 전체 학사일정</h1>
      <div className="grid grid-cols-7 gap-1 text-xs text-center border p-2 rounded">
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
              className={`h-14 border text-[10px] p-1 ${
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
  );
};

export default CalenderPage;
