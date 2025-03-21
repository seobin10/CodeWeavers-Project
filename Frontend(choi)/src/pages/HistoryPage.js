import React from "react";

// props가 없을 때를 대비해서 기본값 지정
const HistoryPage = ({
  timetable = [],
  handleRemove = () => {},
  handleConfirm = () => {},
}) => {
  return (
    <div className="max-w-5xl mx-auto p-4 bg-white shadow-md mt-6 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-6">수강 목록 확인</h2>

      {timetable.length > 0 ? (
        <div className="mt-6">
          <h3 className="text-xl font-semibold mb-4">📚 수강 목록 📚</h3>

          <table className="w-full border border-gray-300">
            <thead>
              <tr className="bg-gray-100 text-center">
                <th className="border p-2">강의명</th>
                <th className="border p-2">요일</th>
                <th className="border p-2">시간</th>
                <th className="border p-2">학점</th>
                <th className="border p-2">교수</th>
                <th className="border p-2">삭제</th>
              </tr>
            </thead>
            <tbody>
              {timetable.map((course) => (
                <tr key={course.강의번호} className="text-center">
                  <td className="border p-2">{course.강의명}</td>
                  <td className="border p-2">{course.강의요일}</td>
                  <td className="border p-2">{course.강의시간}</td>
                  <td className="border p-2">{course.강의학점}</td>
                  <td className="border p-2">{course.담당교수}</td>
                  <td className="border p-2">
                    <button
                      onClick={() => handleRemove(course.강의번호)}
                      className="bg-red-500 hover:bg-red-700 text-white px-3 py-1 rounded text-sm"
                    >
                      삭제 ❌
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* 수강 확정 버튼 */}
          <div className="text-center mt-6">
            <button
              onClick={handleConfirm}
              className="bg-green-600 hover:bg-green-700 text-white font-semibold px-6 py-2 rounded-md shadow-md"
            >
              ✅ 수강 확정
            </button>
          </div>
        </div>
      ) : (
        <p className="text-center text-gray-500 mt-10">수강 목록이 없습니다.</p>
      )}
    </div>
  );
};

export default HistoryPage;