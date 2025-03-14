import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../App";

function LoginPage() {
  const { setUserId } = useContext(AuthContext);
  const [userId, setLocalUserId] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    setMessage("");
    try {
      const response = await fetch("http://localhost:8080/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, password }),
      });

      if (!response.ok) throw new Error("로그인 실패");

      const userData = await response.json();
      setUserId(userData.userId);
      navigate("/main");
    } catch (error) {
      setMessage("로그인에 실패했습니다.");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-center mb-4">로그인</h2>
        {message && <p className="text-red-500 text-center">{message}</p>}

        <input
          type="text"
          placeholder="학번"
          value={userId}
          onChange={(e) => setLocalUserId(e.target.value)}
          className="w-full p-2 border rounded mb-3"
        />

        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full p-2 border rounded mb-3"
        />

        <ul className="flex justify-center gap-3 mt-2">
          <li className="border border-gray-300 px-3 py-2 rounded bg-gray-50">
            <a
              href="/find/userId"
              onClick={(e) => {
                e.preventDefault();
                window.open("/find/userId", "_blank", "width=500, height=300");
              }}
              className="text-gray-500 hover:text-gray-700"
            >
              학번 찾기
            </a>
          </li>
          <li className="border border-gray-300 px-3 py-2 rounded bg-gray-50">
            <a
              href="/find/password"
              onClick={(e) => {
                e.preventDefault();
                window.open(
                  "/find/password",
                  "_blank",
                  "width=500, height=350"
                );
              }}
              className="text-gray-500 hover:text-gray-700"
            >
              비밀번호 찾기
            </a>
          </li>
        </ul>

        <button
          onClick={handleLogin}
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-700 mt-3"
        >
          로그인
        </button>
      </div>
    </div>
  );
}

export default LoginPage;
