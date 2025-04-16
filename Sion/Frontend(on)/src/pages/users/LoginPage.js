import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { loginPostAsync } from "../../slices/authSlice";
import { findUserId, findUserPw } from "../../api/memberApi";

const images = [
  "/images/u1.jpg",
  "/images/u2.jpg",
  "/images/u3.jpg",
  "/images/u4.jpg",
  "/images/u5.jpg",
];

function LoginPage() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [user, setUser] = useState({
    userId: localStorage.getItem("savedUserId") || "",
    userPassword: "",
    remember: !!localStorage.getItem("savedUserId"),
  });

  const [showPassword, setShowPassword] = useState(false);
  const [message, setMessage] = useState("");
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showFindIdModal, setShowFindIdModal] = useState(false);
  const [showFindPwModal, setShowFindPwModal] = useState(false);
  const [showResultModal, setShowResultModal] = useState(false);
  const [resultMessage, setResultMessage] = useState("");

  const [findIdForm, setFindIdForm] = useState({
    userName: "",
    userEmail: "",
    userPhone: "",
  });

  const [findPwForm, setFindPwForm] = useState({
    userId: "",
    userEmail: "",
  });

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setUser((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleLogin = async () => {
    setMessage("");
    try {
      const result = await dispatch(
        loginPostAsync({
          userId: user.userId,
          userPassword: user.userPassword,
        })
      ).unwrap();

      if (user.remember) {
        localStorage.setItem("savedUserId", user.userId);
      } else {
        localStorage.removeItem("savedUserId");
      }
      navigate("/main");
    } catch (error) {
      console.error("로그인 실패:", error.message);
      setMessage("로그인에 실패했습니다.");
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleLogin();
  };

  const handleFindIdSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await findUserId(findIdForm);
      setResultMessage(data.message || "학번: " + data);
      setShowResultModal(true);
      setShowFindIdModal(false);
    } catch {
      setResultMessage("학번 찾기에 실패했습니다.");
      setShowResultModal(true);
    }
  };

  const handleFindPwSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await findUserPw(findPwForm);

      setResultMessage(
        data.password
          ? "비밀번호: " + data.password
          : data.message || "비밀번호 찾기에 실패했습니다."
      );

      setShowResultModal(true);
      setShowFindPwModal(false);
    } catch (error) {
      setResultMessage(error.message);
      setShowResultModal(true);
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

      <div className="absolute inset-0 flex items-center justify-center bg-gray-900 bg-opacity-40">
        <div className="w-full max-w-md bg-white bg-opacity-90 p-6 rounded-lg shadow-md">
          <img
            src="/images/eonLogo.jpg"
            alt="학교 로고"
            className="w-20 h-20 rounded-full mx-auto mb-4"
          />

          {message && (
            <p className="text-red-500 text-center mb-2">{message}</p>
          )}

          <div className="bg-white border border-gray-300 rounded px-3 py-2 mb-3 flex items-center">
            <img
              src="/images/id.jpg"
              alt="User Icon"
              className="w-6 h-6 mr-2"
            />
            <input
              type="text"
              name="userId"
              placeholder="학번"
              value={user.userId}
              onChange={handleChange}
              onKeyDown={handleKeyPress}
              className="w-3/4 outline-none bg-white"
            />
            <label className="flex items-center gap-2 whitespace-nowrap">
              <input
                type="checkbox"
                name="remember"
                className="w-4 h-3"
                checked={user.remember}
                onChange={handleChange}
              />
              <span className="text-sm text-gray-500">학번 저장</span>
            </label>
          </div>

          <div className="bg-white border border-gray-300 rounded px-3 py-2 mb-3 flex items-center relative">
            <img
              src="/images/pw.jpg"
              alt="Lock Icon"
              className="w-6 h-6 mr-2"
            />
            <input
              type={showPassword ? "text" : "password"}
              name="userPassword"
              placeholder="비밀번호"
              value={user.userPassword}
              onChange={handleChange}
              onKeyDown={handleKeyPress}
              className="w-full outline-none bg-white pr-8"
            />
            <button
              type="button"
              onClick={() => setShowPassword((prev) => !prev)}
              className="absolute right-3 top-1/2 transform -translate-y-1/2"
            >
              <img
                src={showPassword ? "/images/showPw.png" : "/images/HidePw.png"}
                alt="비밀번호 보기 토글"
                className="w-5 h-5 opacity-70 hover:opacity-100"
              />
            </button>
          </div>

          <ul className="flex justify-end text-sm mb-4">
            <li className="px-3">
              <button
                onClick={() => setShowFindIdModal(true)}
                className="text-gray-400 hover:text-gray-800"
              >
                학번 찾기
              </button>
            </li>
            <li className="px-3">
              <button
                onClick={() => setShowFindPwModal(true)}
                className="text-gray-400 hover:text-gray-800"
              >
                비밀번호 찾기
              </button>
            </li>
          </ul>

          <button
            onClick={handleLogin}
            className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-800"
          >
            로그인
          </button>

          <div className="text-xs text-gray-500 mt-4 text-left">
            <ul>
              <li>
                * 최초 로그인 시 반드시 비밀번호를 변경해 주시기 바랍니다.
              </li>
              <li>
                * 신입생의 경우, 초기 비밀번호는 생년월일 6자리 + ! 입니다.
              </li>
              <li>
                * 비밀번호를 분실한 경우에는 ‘비밀번호 찾기’를 이용하세요.
              </li>
            </ul>
          </div>
        </div>
      </div>

      {showFindIdModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white bg-opacity-90 p-8 rounded-lg w-full max-w-md relative">
            <button
              className="absolute top-2 right-2 text-gray-500"
              onClick={() => setShowFindIdModal(false)}
            >
              ✕
            </button>
            <form onSubmit={handleFindIdSubmit} className="space-y-5">
              <h2 className="text-2xl font-bold text-center mb-2">학번 찾기</h2>
              <input
                name="userName"
                placeholder="이름"
                className="w-full p-2 border rounded focus:outline-none focus:ring-0"
                required
                onChange={(e) =>
                  setFindIdForm({ ...findIdForm, userName: e.target.value })
                }
              />
              <input
                name="userPhone"
                placeholder="010-0000-0000"
                className="w-full p-2 border rounded focus:outline-none focus:ring-0"
                required
                onChange={(e) =>
                  setFindIdForm({ ...findIdForm, userPhone: e.target.value })
                }
              />
              <input
                name="userEmail"
                placeholder="이메일"
                className="w-full p-2 border rounded focus:outline-none focus:ring-0"
                required
                onChange={(e) =>
                  setFindIdForm({ ...findIdForm, userEmail: e.target.value })
                }
              />
              <button
                type="submit"
                className="w-full bg-blue-400 text-white py-2 rounded hover:bg-blue-800"
              >
                학번 찾기
              </button>
            </form>
          </div>
        </div>
      )}

      {showFindPwModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white bg-opacity-90 p-6 rounded-lg w-full max-w-md relative">
            <button
              className=" absolute top-2 right-2 text-gray-500"
              onClick={() => setShowFindPwModal(false)}
            >
              ✕
            </button>
            <form onSubmit={handleFindPwSubmit} className="space-y-5">
              <h2 className="text-2xl font-bold text-center mb-2">
                비밀번호 찾기
              </h2>
              <input
                name="userId"
                placeholder="학번"
                className="w-full p-2 border rounded focus:outline-none focus:ring-0"
                required
                onChange={(e) =>
                  setFindPwForm({ ...findPwForm, userId: e.target.value })
                }
              />
              <input
                name="userEmail"
                placeholder="이메일"
                className="w-full p-2 border rounded focus:outline-none focus:ring-0"
                required
                onChange={(e) =>
                  setFindPwForm({ ...findPwForm, userEmail: e.target.value })
                }
              />
              <button
                type="submit"
                className="w-full bg-blue-400 text-white py-2 rounded hover:bg-blue-800"
              >
                비밀번호 찾기
              </button>
            </form>
          </div>
        </div>
      )}

      {showResultModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-sm shadow-md relative">
            <button
              className="absolute top-2 right-2 text-gray-500"
              onClick={() => setShowResultModal(false)}
            >
              ✕
            </button>
            <p className="text-center text-gray-800">{resultMessage}</p>
            <button
              className="mt-4 w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-700"
              onClick={() => setShowResultModal(false)}
            >
              확인
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default LoginPage;
