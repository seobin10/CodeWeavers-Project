import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { AuthContext } from "../App";

const EnrollmentPage = () => {
  const { userId, setUserId } = useContext(AuthContext);
  const [courses, setCourses] = useState([]);
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
        ] = await Promise.all([
          axios.get(
            "http://localhost:8080/api/students/enrollment/departments"
          ),
          axios.get(
            "http://localhost:8080/api/students/enrollment/courseTypes"
          ),
          axios.get(
            "http://localhost:8080/api/students/enrollment/courseYears"
          ),
          axios.get("http://localhost:8080/api/students/enrollment/classDays"),
          axios.get("http://localhost:8080/api/students/enrollment/classTimes"),
          axios.get("http://localhost:8080/api/students/enrollment/credits"),
        ]);

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
    if (!userId) return;

    const loadData = async (userId) => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/students/enrollment/${userId}/enrollment`,
          {
            params: {
              courseType: filterCategory !== "전체" ? filterCategory : null,
              departmentName:
                filterDepartment !== "전체" ? filterDepartment : null,
              courseYear: filterYear !== "전체" ? parseInt(filterYear) : null,
              classDay: filterDay !== "전체" ? filterDay : null,
              classStart: filterTime !== "전체" ? parseInt(filterTime) : null,
              credit: filterCredit !== "전체" ? parseInt(filterCredit) : null,
              courseName: searchQuery !== "" ? searchQuery : null,
            },
          }
        );

        setCourses(response.data);
      } catch (error) {
        console.error("데이터 불러오기 실패:", error);
      }
    };
    loadData();
  }, [
    userId,
    filterCategory,
    filterDepartment,
    filterYear,
    filterDay,
    filterTime,
    filterCredit,
    searchQuery,
  ]);

  return (
    <div className="max-w-5xl mx-auto p-6 bg-white shadow-md mt-4 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-6">수강 신청</h2>

      <div className="space-y-4">
        <div className="grid grid-cols-3 gap-4">
          <select
            className="border p-2 rounded w-full"
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
            className="border p-2 rounded w-full"
            value={filterDepartment}
            onChange={(e) => {
              setFilterDepartment(e.target.value);
            }}
          >
            <option value="전체">전체 학과</option>
            {filters.departments.map((dept) => (
              <option key={dept.departmentName} value={dept.departmentName}>
                {dept.departmentName}
              </option>
            ))}
          </select>
          <select
            className="border p-2 rounded w-full"
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
        </div>
      </div>
      <div className="space-y-4">
        <div className="grid grid-cols-3 gap-4">
          <select
            className="border p-2 rounded w-full"
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
            className="border p-2 rounded w-full"
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
            className="border p-2 rounded w-full"
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

        <input
          type="text"
          className="border p-2 rounded w-full"
          placeholder="강의명 검색"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      <table className="w-full border-collapse border border-gray-300 mt-6">
        <thead>
          <tr className="bg-gray-100 text-center">
            <th className="border border-gray-300 p-2">강의번호</th>
            <th className="border border-gray-300 p-2">구분</th>
            <th className="border border-gray-300 p-2">개설학과</th>
            <th className="border border-gray-300 p-2">강의학년</th>
            <th className="border border-gray-300 p-2">강의명</th>
            <th className="border border-gray-300 p-2">강의요일</th>
            <th className="border border-gray-300 p-2">강의실</th>
            <th className="border border-gray-300 p-2">강의시간</th>
            <th className="border border-gray-300 p-2">학점</th>
            <th className="border border-gray-300 p-2">담당교수</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course) => (
            <tr key={course.강의번호} className="text-center">
              <td className="border border-gray-300 p-2">{course.강의번호}</td>
              <td className="border border-gray-300 p-2">{course.구분}</td>
              <td className="border border-gray-300 p-2">{course.개설학과}</td>
              <td className="border border-gray-300 p-2">{course.강의학년}</td>
              <td className="border border-gray-300 p-2">{course.강의명}</td>
              <td className="border border-gray-300 p-2">{course.강의요일}</td>
              <td className="border border-gray-300 p-2">{course.강의실}</td>
              <td className="border border-gray-300 p-2">{course.강의시간}</td>
              <td className="border border-gray-300 p-2">{course.강의학점}</td>
              <td className="border border-gray-300 p-2">{course.담당교수}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EnrollmentPage;
