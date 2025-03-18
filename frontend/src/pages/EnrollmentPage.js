import React, { useEffect, useState } from "react";
import axios from "axios";

const EnrollmentPage = () => {
  const [courses, setCourses] = useState([]);
  const [selectedCourses, setSelectedCourses] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterDepartment, setFilterDepartment] = useState("전체");
  const [filterGrade, setFilterGrade] = useState("전체");

  useEffect(() => {
    fetchCourses();
    fetchEnrolledCourses();
  }, []);

  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses")
      .then((response) => {
        setCourses(response.data);
      })
      .catch((error) => {
        console.error("강의 목록 조회 실패:", error);
      });
  };

  const fetchEnrolledCourses = () => {
    axios
      .get("http://localhost:8080/api/enrollment")
      .then((response) => {
        setSelectedCourses(response.data.map((course) => course.courseId));
      })
      .catch((error) => {
        console.error("수강신청 조회 실패:", error);
      });
  };

  const handleEnroll = async (courseId) => {
    try {
      await axios.post("http://localhost:8080/api/enrollment", { courseId });
      alert("수강 신청이 완료되었습니다.");
      fetchEnrolledCourses(); // 수강 신청 후 즉시 반영
    } catch (error) {
      console.error("수강 신청 실패:", error);
      alert("수강 신청에 실패했습니다.");
    }
  };

  const filteredCourses = courses.filter(
    (course) =>
      (filterDepartment === "전체" || course.department === filterDepartment) &&
      (filterGrade === "전체" || course.grade === filterGrade) &&
      (searchQuery === "" || course.name.includes(searchQuery))
  );

  return (
    <div className="max-w-5xl mx-auto p-6 bg-white shadow-md mt-4 rounded-md">
      <h2 className="text-2xl font-bold text-center mb-4">수강 신청</h2>
      <div className="flex space-x-4 mb-4">
        <select
          className="border p-2 rounded"
          value={filterDepartment}
          onChange={(e) => setFilterDepartment(e.target.value)}
        >
          <option value="전체">전체 학과</option>
          <option value="컴퓨터공학과">컴퓨터공학과</option>
          <option value="전자공학과">전자공학과</option>
        </select>
        <select
          className="border p-2 rounded"
          value={filterGrade}
          onChange={(e) => setFilterGrade(e.target.value)}
        >
          <option value="전체">전체 학년</option>
          <option value="1학년">1학년</option>
          <option value="2학년">2학년</option>
          <option value="3학년">3학년</option>
          <option value="4학년">4학년</option>
        </select>
        <input
          type="text"
          className="border p-2 rounded w-full"
          placeholder="검색어 입력..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>
      <table className="w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100 text-center">
            <th className="border border-gray-300 p-2">강의번호</th>
            <th className="border border-gray-300 p-2">학과</th>
            <th className="border border-gray-300 p-2">학년</th>
            <th className="border border-gray-300 p-2">강의명</th>
            <th className="border border-gray-300 p-2">강의실</th>
            <th className="border border-gray-300 p-2">강의시간</th>
            <th className="border border-gray-300 p-2">학점</th>
            <th className="border border-gray-300 p-2">신청</th>
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
              <td className="border border-gray-300 p-2">
                {selectedCourses.includes(course.id) ? (
                  <span className="text-green-600 font-bold">신청됨</span>
                ) : (
                  <button
                    onClick={() => handleEnroll(course.id)}
                    className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-700"
                  >
                    담기
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EnrollmentPage;
