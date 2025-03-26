import { useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../App";

const images = [
  "/images/u1.jpg",
  "/images/u2.jpg",
  "/images/u3.jpg",
  "/images/u4.jpg",
  "/images/u5.jpg",
];

function LoginPage() {
  const { setUserId } = useContext(AuthContext);
  const [userId, setLocalUserId] = useState(
    localStorage.getItem("savedUserId") || ""
  );
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [rememberUserId, setRememberUserId] = useState(
    !!localStorage.getItem("savedUserId")
  );
  const navigate = useNavigate();
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    const sliderInterval = 3000;
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, sliderInterval);

    return () => clearInterval(interval);
  }, []);

  const handleRememberUserId = (e) => {
    const isChecked = e.target.checked;
    setRememberUserId(isChecked);

    if (isChecked) {
      localStorage.setItem("savedUserId", userId);
    } else {
      localStorage.removeItem("savedUserId");
    }
  };

  const handleLogin = async () => {
    setMessage("");
    try {
      const response = await fetch("http://localhost:8080/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, userPassword: password }),
      });

      if (!response.ok) throw new Error("로그인 실패");

      const userData = await response.json();
      setUserId(userData.userId);

      if (rememberUserId) {
        localStorage.setItem("savedUserId", userId);
      }

      navigate("/main");
    } catch (error) {
      setMessage("로그인에 실패했습니다.");
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleLogin();
    }
  };

  return (
    <div className="relative w-full h-screen overflow-hidden">
      <div className="absolute top-0 left-0 w-full h-full">
        {images.map((src, index) => (
          <img
            key={index}
            src={src}
            className="absolute top-0 left-0 w-full h-full object-cover transition-opacity duration-700"
            alt={`bg-${index}`}
            style={{ opacity: index === currentIndex ? 1 : 0 }}
          />
        ))}
      </div>

      <div className="absolute inset-0 flex flex-col items-center justify-center bg-gray-900 bg-opacity-40">
        <div className="w-full max-w-md bg-white bg-opacity-90 p-6 rounded-lg shadow-md flex flex-col items-center">
          <img
            src="/images/eonLogo.jpg"
            alt="학교 로고"
            className="w-20 h-20 rounded-full mb-4"
          />

          {message && (
            <p className="text-red-500 text-center mb-2">{message}</p>
          )}

          <div className="w-full">
            <div className="w-full bg-white border border-gray-300 rounded px-3 py-2 mb-3 flex items-center">
              <img
                src="/images/id.jpg"
                alt="User Icon"
                className="w-6 h-6 mr-2"
              />
              <input
                type="text"
                placeholder="학번"
                value={userId}
                onChange={(e) => setLocalUserId(e.target.value)}
                onKeyDown={handleKeyPress}
                className="w-3/4 outline-none bg-white"
              />
              <label className="flex items-center gap-2 whitespace-nowrap">
                <input
                  type="checkbox"
                  className="w-4 h-3"
                  checked={rememberUserId}
                  onChange={handleRememberUserId}
                />
                <span className="text-sm text-gray-500">학번 저장</span>
              </label>
            </div>

            <div className="w-full bg-white border border-gray-300 rounded px-3 py-2 mb-3 flex items-center">
              <img
                src="/images/pw.jpg"
                alt="Lock Icon"
                className="w-6 h-6 mr-2"
              />
              <input
                type="password"
                placeholder="비밀번호"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                onKeyDown={handleKeyPress}
                className="w-full outline-none bg-white"
              />
            </div>
          </div>

          <ul className="flex justify-end w-full">
            <li className="px-3 py-2 rounded">
              <button
                onClick={() => navigate("/member/findId")}
                className="text-gray-400 hover:text-gray-800 text-sm"
              >
                학번 찾기
              </button>
            </li>
            <li className="px-3 py-2 rounded">
              <button
                onClick={() => navigate("/member/findPw")}
                className="text-gray-400 hover:text-gray-800 text-sm"
              >
                비밀번호 찾기
              </button>
            </li>
          </ul>
          <button
            onClick={handleLogin}
            className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-800 mt-3"
          >
            로그인
          </button>
          <br />
          <div className="text-xs text-gray-500 mt-4 text-left">
            <ul>
              <li>
                * 최초 로그인 시 반드시 비밀번호를 변경해 주시기 바랍니다.
              </li>
              <li>
                * 신입생의 경우, 초기 비밀번호는 주민등록번호 앞자리입니다.
              </li>
              <li>
                * 비밀번호를 분실한 경우에는 ‘비밀번호 찾기’를 이용하시기
                바랍니다.
              </li>
            </ul>
            <br />
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
