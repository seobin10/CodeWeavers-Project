// src/pages/EnrollmentPage.jsx
import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../App";
import {
  getFilters,
  searchCourses,
  enrollCourse,
  getMyCourses,
} from "../api/enrollmentApi";

const EnrollmentPage = () => {
  const { userId, setUserId } = useContext(AuthContext);
  const [courses, setCourses] = useState([]);
  const [timetable, setTimetable] = useState([]);

  const [filters, setFilters] = useState({
    departments: [],
    courseTypes: [],
    courseYears: [],
    classDays: [],
    classTimes: [],
    credits: [],
  });

  const [searchQuery, setSearchQuery] = useState("");
  const [filterCategory, setFilterCategory] = useState("전체");
  const [filterDepartment, setFilterDepartment] = useState("전체");
  const [filterYear, setFilterYear] = useState("전체");
  const [filterDay, setFilterDay] = useState("전체");
  const [filterTime, setFilterTime] = useState("전체");
  const [filterCredit, setFilterCredit] = useState("전체");

  useEffect(() => {
    const localId = localStorage.getItem("id");
    if (!userId && localId) {
      setUserId(localId);
    }

    const fetchFilters = async () => {
      try {
        const [
          departmentsRes,
          courseTypesRes,
          courseYearsRes,
          classDaysRes,
          classTimesRes,
          creditsRes,
        ] = await getFilters();

        setFilters({
          departments: departmentsRes.data,
          courseTypes: courseTypesRes.data,
          courseYears: courseYearsRes.data,
          classDays: classDaysRes.data,
          classTimes: classTimesRes.data,
          credits: creditsRes.data,
        });
      } catch (error) {
        console.error("필터 데이터 불러오기 실패:", error);
      }
    };

    fetchFilters();
  }, [userId, setUserId]);

  useEffect(() => {
    const fetchMyEnrollments = async () => {
      try {
        const response = await getMyCourses(userId);
        setTimetable(response.data);
      } catch (error) {
        console.error("내 수강 목록 불러오기 실패:", error);
      }
    };

    if (userId) {
      fetchMyEnrollments();
    }
  }, [userId]);

  const handleSearch = async () => {
    try {
      const response = await searchCourses(userId, {
        courseName: searchQuery !== "" ? searchQuery : null,
        courseType: filterCategory !== "전체" ? filterCategory : null,
        departmentName: filterDepartment !== "전체" ? filterDepartment : null,
        courseYear: filterYear !== "전체" ? parseInt(filterYear) : null,
        classDay: filterDay !== "전체" ? filterDay : null,
        classStart: filterTime !== "전체" ? parseInt(filterTime) : null,
        credit: filterCredit !== "전체" ? parseInt(filterCredit) : null,
      });

      setCourses(response.data);
    } catch (error) {
      console.error("데이터 불러오기 실패:", error);
    }
  };

  const handleEnroll = async (course) => {
    const isAlreadyEnrolled = timetable.some(
      (c) => c.강의번호 === course.강의번호
    );
    if (isAlreadyEnrolled) {
      alert("이미 시간표에 추가된 강의입니다!");
      return;
    }
  
    try {
      const response = await enrollCourse(userId, {
        studentId: userId,
        classId: course.강의번호,
      });
    
      const msg = typeof response.data === "string" 
        ? response.data 
        : response.data.message ?? "응답 메시지를 확인할 수 없습니다.";
    
      alert(msg); 
    

      if (msg === "수강 신청에 성공하였습니다") {
        // const updated = await getMyCourses(userId);  미구현
        // setTimetable(updated.data);
      }
    
    } catch (error) {
      console.error("수강 신청 실패:", error);
    
      const msg = error.response?.data?.message 
        ?? error.response?.data 
        ?? error.message 
        ?? "수강 신청 중 오류가 발생했습니다.";
      
      alert(msg);
    }
  };
  return (
    <div className="max-w-7xl mx-auto p-2 bg-white shadow-md mt-3 rounded-md">
      <h2 className="text-3xl font-bold text-center mb-6 mt-3">
        수강 신청 목록
      </h2>

      {/* 필터 영역 */}
      <div className="space-y-4">
        <div className="grid grid-cols-3 gap-4 mb-4">
          <select
            className="bg-blue-50 border p-2 rounded w-full"
            value={filterCategory}
            onChange={(e) => setFilterCategory(e.target.value)}
          >
            <option value="전체">전체 구분</option>
            {filters.courseTypes.map((type) => (
              <option key={type.courseType} value={type.courseType}>
                {type.courseType}
              </option>
            ))}
          </select>
          <select
            className="bg-blue-50 border p-2 rounded w-full"
            value={filterDepartment}
            onChange={(e) => setFilterDepartment(e.target.value)}
          >
            <option value="전체">전체 학과</option>
            {filters.departments.map((dept) => (
              <option key={dept.departmentName} value={dept.departmentName}>
                {dept.departmentName}
              </option>
            ))}
          </select>
          <select
            className="bg-blue-50 border p-2 rounded w-full"
            value={filterYear}
            onChange={(e) => setFilterYear(e.target.value)}
          >
            <option value="전체">전체 학년</option>
            {filters.courseYears.map((year) => (
              <option key={year.courseYear} value={year.courseYear}>
                {year.courseYear}학년
              </option>
            ))}
          </select>

          <select
            className="bg-blue-50 border p-2 rounded w-full"
            value={filterDay}
            onChange={(e) => setFilterDay(e.target.value)}
          >
            <option value="전체">전체 요일</option>
            {filters.classDays.map((day) => (
              <option key={day.classDay} value={day.classDay}>
                {day.classDay}
              </option>
            ))}
          </select>

          <select
            className="bg-blue-50 border p-2 rounded w-full"
            value={filterTime}
            onChange={(e) => setFilterTime(e.target.value)}
          >
            <option value="전체">전체 강의시간</option>
            {filters.classTimes.map((time) => (
              <option key={time.classTime} value={time.classTime}>
                {time.classTime}교시
              </option>
            ))}
          </select>

          <select
            className="bg-blue-50 border p-2 rounded w-full"
            value={filterCredit}
            onChange={(e) => setFilterCredit(e.target.value)}
          >
            <option value="전체">전체 학점</option>
            {filters.credits.map((credit) => (
              <option key={credit.credit} value={credit.credit}>
                {credit.credit}학점
              </option>
            ))}
          </select>
        </div>

        {/* 검색창 + 버튼 */}
        <div className="grid grid-cols-4 gap-4 items-center">
          <div className="col-span-4 relative w-full">
            <input
              type="text"
              className="bg-blue-50 border p-2 rounded w-full"
              placeholder="강의명 검색"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button
              onClick={handleSearch}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-blue-600 text-white py-2 px-4 rounded text-sm hover:bg-blue-800"
            >
              검색 🔍
            </button>
          </div>
        </div>

        {/* 검색 결과 */}
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100 text-center">
              <th className="border p-2">강의번호</th>
              <th className="border p-2">구분</th>
              <th className="border p-2">개설학과</th>
              <th className="border p-2">강의학년</th>
              <th className="border p-2">강의명</th>
              <th className="border p-2">강의요일</th>
              <th className="border p-2">강의실</th>
              <th className="border p-2">강의시간</th>
              <th className="border p-2">학점</th>
              <th className="border p-2">담당교수</th>
              <th className="border p-2">신청인원/정원</th>
              <th className="border p-2">담기</th>
            </tr>
          </thead>
          <tbody>
            {courses.map((course) => (
              <tr key={course.강의번호} className="text-center">
                <td className="border p-2">{course.강의번호}</td>
                <td className="border p-2">{course.구분}</td>
                <td className="border p-2">{course.개설학과}</td>
                <td className="border p-2">{course.강의학년}</td>
                <td className="border p-2">{course.강의명}</td>
                <td className="border p-2">{course.강의요일}</td>
                <td className="border p-2">{course.강의실}</td>
                <td className="border p-2">{course.강의시간}</td>
                <td className="border p-2">{course.강의학점}</td>
                <td className="border p-2">{course.담당교수}</td>
                <td className="border p-2">{course.수강인원}</td>

                <td className="border p-2">
                  <button
                    onClick={() => handleEnroll(course)}
                    className="bg-blue-400 hover:bg-blue-700 text-white px-3 py-1 rounded text-sm"
                  >
                    담기 🛒
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default EnrollmentPage;
