import { useState, useContext, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../App";
import axios from "axios";

function LoginPage() {
  const { setUserId } = useContext(AuthContext);
  const [userId, setLocalUserId] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();
  

  const localIsNotNull =
    localStorage.getItem("id") !== null && localStorage.getItem("pw") !== null;


  const handleLogin = async () => {
    setMessage("");


    console.log("로그인 요청 데이터:", { userId, userPassword });

    try {
      const response = await axios.post("http://localhost:8080/api/user/login", {
        userId,
        userPassword,
      });


      localStorage.setItem("id", userId);
      localStorage.setItem("pw", userPassword);
      setUserId(response.data.userId);
      navigate("/main");
    } catch (error) {
      console.error("서버 응답 에러:", error.response);

      if (error.response) {
        if (error.response.status === 401) {
          console.error("로그인 실패: 비밀번호가 틀림");
          setMessage("비밀번호가 일치하지 않습니다.");
        } else if (error.response.status === 404) {
          console.error("로그인 실패: 존재하지 않는 사용자");
          setMessage("존재하지 않는 사용자입니다.");
        } else {
          console.error("로그인 요청 실패:", error);
          setMessage("로그인에 실패했습니다.");
        }
      } else {
        console.error("서버 연결 실패");
        setMessage("서버에 연결할 수 없습니다.");
      }
    }
  };


  const autoLogin = useCallback(async (localId, localPw) => {
    setMessage("");

    console.log("자동 로그인 시도 - ID:", localId, "PW:", localPw);

    if (!localId || !localPw) {
      console.log("자동 로그인 실패 - 저장된 ID/PW가 없음");
      return;
    }

    try {
      const response = await axios.post("http://localhost:8080/api/user/login", {
        userId: localId,
        userPassword: localPw,
      });

      setUserId(response.data.userId);
      navigate("/main");
    } catch (error) {
      console.error("자동 로그인 실패:", error.response);
      setMessage("자동 로그인에 실패했습니다.");
    }
  }, [navigate, setUserId]);


  useEffect(() => {
    if (localIsNotNull) {
      let localId = localStorage.getItem("id");
      let localPw = localStorage.getItem("pw");


      if (localId && localPw) {
        autoLogin(localId, localPw);
      }
    }
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
