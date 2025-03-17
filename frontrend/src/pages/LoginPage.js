import { useState, useContext, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../App";

function LoginPage() {
  const { setUserId } = useContext(AuthContext);
  const [userId, setLocalUserId] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();
  const localIsNotNull = localStorage.getItem("id") != null && localStorage.getItem("pw");

  const handleLogin = async () => {
    setMessage("");
    try {
      const response = await fetch("http://localhost:8080/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, userPassword }),
      });

      if (!response.ok) throw new Error("로그인 실패");
      else {
        localStorage.setItem("id", userId);
        localStorage.setItem("pw", userPassword);
      }

      const userData = await response.json();
      setUserId(userData.userId);

      navigate("/main");
    } catch (error) {
      setMessage("로그인에 실패했습니다.");
    }
  };

  const autoLogin = useCallback(
    async (localId, localPw) => {
      setMessage("");
      try {
        const response = await fetch("http://localhost:8080/api/user/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ userId: localId, password: localPw }),
        });

        const userData = await response.json();
        setUserId(userData.userId);
        navigate("/main");
      } catch (error) {
        setMessage("로그인에 실패했습니다.");
      }
    },
    [navigate, setUserId]
  );

  useEffect(() => {
    if (localIsNotNull) {
      let localId = localStorage.getItem("id");
      let localPw = localStorage.getItem("pw");
      autoLogin(localId, localPw);
    }
    return () => {};
  }, [autoLogin, localIsNotNull]);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-center mb-4">로그인</h2>
        {message && <p className="text-red-500 text-center">{message}</p>}
        <input
          type="text"
          placeholder="사용자 ID"
          value={userId}
          onChange={(e) => setLocalUserId(e.target.value)}
          className="w-full p-2 border rounded mb-3"
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={userPassword}
          onChange={(e) => setUserPassword(e.target.value)}
          className="w-full p-2 border rounded mb-3"
        />
        <button
          onClick={handleLogin}
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-700"
        >
          로그인
        </button>
      </div>
    </div>
  );
}

export default LoginPage;
