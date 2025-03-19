import React, { useEffect, useState } from "react";
import axios from "axios";

const EnrollmentPage = () => {
  const [courses, setCourses] = useState([]);
  const [selectedCourses, setSelectedCourses] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterCategory, setFilterCategory] = useState("전체");
  const [filterDepartment, setFilterDepartment] = useState("전체");
  const [filterGrade, setFilterGrade] = useState("전체");
  const [filterDay, setFilterDay] = useState("전체");
  const [filterTime, setFilterTime] = useState("전체");
  const [filterCredit, setFilterCredit] = useState("전체");

  useEffect(() => {
    const loadData = async () => {
      try {
        const [coursesResponse, enrolledResponse] = await Promise.all([
          axios.get("http://localhost:8080/api/courses"),
          axios.get("http://localhost:8080/api/enrollment"),
        ]);
        setCourses(coursesResponse.data);
        setSelectedCourses(
          enrolledResponse.data.map((course) => course.courseId)
        );
      } catch (error) {
        console.error("데이터 불러오기 실패:", error);
      }
    };
    loadData();
  }, []);

  const filteredCourses = courses.filter(
    (course) =>
      (filterCategory === "전체" || course.category === filterCategory) &&
      (filterDepartment === "전체" || course.department === filterDepartment) &&
      (filterGrade === "전체" || course.grade === filterGrade) &&
      (filterDay === "전체" || course.schedule.includes(filterDay)) &&
      (filterTime === "전체" || course.schedule.includes(filterTime)) &&
      (filterCredit === "전체" || course.credit.toString() === filterCredit) &&
      (searchQuery === "" ||
        course.name.includes(searchQuery) ||
        course.id.toString().includes(searchQuery))
  );

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-md mt-4 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-6">수강 신청</h2>

      {/* 기존 UI 유지 */}
      <div className="space-y-4">
        <div className="grid grid-cols-3 gap-4">
          <select
            className="border p-2 rounded w-full"
            value={filterCategory}
            onChange={(e) => setFilterCategory(e.target.value)}
          >
            <option value="전체">전체</option>
            <option value="전공">전공</option>
            <option value="교양">교양</option>
          </select>
          <select
            className="border p-2 rounded w-full"
            value={filterDepartment}
            onChange={(e) => setFilterDepartment(e.target.value)}
          >
            <option value="전체">전체 학과</option>
            <option value="컴퓨터공학과">컴퓨터공학과</option>
            <option value="전자공학과">전자공학과</option>
          </select>
          <select
            className="border p-2 rounded w-full"
            value={filterGrade}
            onChange={(e) => setFilterGrade(e.target.value)}
          >
            <option value="전체">전체 학년</option>
            <option value="1학년">1학년</option>
            <option value="2학년">2학년</option>
            <option value="3학년">3학년</option>
            <option value="4학년">4학년</option>
          </select>
        </div>

        <div className="grid grid-cols-3 gap-4">
          <select
            className="border p-2 rounded w-full"
            value={filterDay}
            onChange={(e) => setFilterDay(e.target.value)}
          >
            <option value="전체">전체 요일</option>
            <option value="월">월요일</option>
            <option value="화">화요일</option>
            <option value="수">수요일</option>
            <option value="목">목요일</option>
            <option value="금">금요일</option>
          </select>
          <select
            className="border p-2 rounded w-full"
            value={filterTime}
            onChange={(e) => setFilterTime(e.target.value)}
          >
            <option value="전체">전체 강의시간</option>
            <option value="1">1교시</option>
            <option value="2">2교시</option>
            <option value="3">3교시</option>
            <option value="4">4교시</option>
            <option value="5">5교시</option>
            <option value="6">6교시</option>
          </select>
          <select
            className="border p-2 rounded w-full"
            value={filterCredit}
            onChange={(e) => setFilterCredit(e.target.value)}
          >
            <option value="전체">전체 학점</option>
            <option value="1">1학점</option>
            <option value="2">2학점</option>
            <option value="3">3학점</option>
            <option value="4">4학점</option>
          </select>
        </div>

        <input
          type="text"
          className="border p-2 rounded w-full"
          placeholder="강의번호 또는 강의명 검색"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* 강의 리스트 */}
      <table className="w-full border-collapse border border-gray-300 mt-6">
        <thead>
          <tr className="bg-gray-100 text-center">
            <th className="border border-gray-300 p-2">강의번호</th>
            <th className="border border-gray-300 p-2">학과</th>
            <th className="border border-gray-300 p-2">학년</th>
            <th className="border border-gray-300 p-2">강의명</th>
            <th className="border border-gray-300 p-2">강의실</th>
            <th className="border border-gray-300 p-2">강의시간</th>
            <th className="border border-gray-300 p-2">학점</th>
          </tr>
        </thead>
        <tbody>
          {filteredCourses.map((course) => (
            <tr key={course.id} className="text-center">
              <td className="border border-gray-300 p-2">{course.id}</td>
              <td className="border border-gray-300 p-2">
                {course.department}
              </td>
              <td className="border border-gray-300 p-2">{course.grade}</td>
              <td className="border border-gray-300 p-2">{course.name}</td>
              <td className="border border-gray-300 p-2">{course.room}</td>
              <td className="border border-gray-300 p-2">{course.schedule}</td>
              <td className="border border-gray-300 p-2">{course.credit}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EnrollmentPage;
