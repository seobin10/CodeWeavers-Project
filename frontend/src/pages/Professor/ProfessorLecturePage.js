// ProfessorLecturePage.jsx
import React, { useEffect, useState } from "react";

const ProfessorLecturePage = () => {
  const [lectures, setLectures] = useState([]);

  useEffect(() => {
  }, []);

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">내 강의 목록</h2>
    </div>
  );
};

export default ProfessorLecturePage;