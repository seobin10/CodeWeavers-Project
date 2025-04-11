import React, { useState } from "react";
import "../../src/EvaluationPage.css";

const questions = [
  "교수의 수업 준비는 철저했나요?",
  "교수가 열성적으로 강의했나요?",
  "수업은 정해진 시간대로 진행되었나요?",
  "수업 내용이 전반적으로 만족스러웠나요?",
  "과제가 이해 및 지식 습득에 도움 되었나요?",
  "과제 및 시험의 평가가 공정했나요?",
];

const EvaluationPage = () => {
  const [scores, setScores] = useState(Array(questions.length).fill(3));
  const [comment, setComment] = useState("");

  const handleSliderChange = (index, value) => {
    const newScores = [...scores];
    newScores[index] = Number(value);
    setScores(newScores);
  };

  const handleReset = () => {
    setScores(Array(questions.length).fill(3));
    setComment('');
  }

  return (
    <div className="w-full max-w-3xl mx-auto mt-10 p-8 sm:p-10 lg:p-12 bg-white shadow-md rounded-lg">
      <p className="font-bold text-3xl text-blue-700">강의 평가 <span className="font-normal text-sm float-right text-gray-400 pt-8">과목 명 : 창의적 사고</span></p>
      <hr className="mt-7" />
      
      {questions.map((q, idx) => (
        <div className="my-12" key={idx}>
          <label className="block mb-2 text-left">
            <span className="font-bold text-blue-700 text-lg">
              질문 {idx + 1}:
            </span>
            <br /> {q}
          </label>
          <div className="w-[85%] mx-auto">
            <input
              type="range"
              min="1"
              max="5"
              value={scores[idx]}
              onChange={(e) => handleSliderChange(idx, e.target.value)}
              className="custom-slider w-full appearance-none h-2 rounded bg-gradient-to-r from-blue-800 to-teal-200 outline-none mb-1"
            />
            <div className="flex justify-between text-sm px-1 text-gray-600">
              {[1, 2, 3, 4, 5].map((num) => (
                <span key={num}>{num}</span>
              ))}
            </div>
          </div>
        </div>
      ))}

      <div className="my-12">
        <label className="block mb-2 text-left">
          <span className="font-bold text-blue-700 text-lg">질문 7:</span>{" "}
          <br />
          개선 사항 및 교수님께 하고 싶은 말을 적어주세요.
        </label>
        <textarea
          rows="4"
          placeholder="의견을 적어주세요"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          className="w-full p-2 text-base mt-2 resize-y border rounded-md focus:outline-none focus:ring focus:ring-blue-300"
        />
      </div>
      <div className="text-white text-sm font-semibold float-right">
        <button className="bg-blue-500 hover:bg-blue-700 py-2 px-3 rounded transition">
          제출
        </button>
        &nbsp;
        <button className="bg-gray-400 hover:bg-gray-700 py-2 px-3 rounded transition" onClick={handleReset}>
          리셋
        </button>
      </div>
    </div>
  );
};

export default EvaluationPage;
