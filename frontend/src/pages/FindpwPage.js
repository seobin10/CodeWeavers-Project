import { useState } from "react";

function FindpwPage() {
  const [email, setEmail] = useState("");

  const handleFindPassword = async (e) => {
    e.preventDefault();
    if (!email) {
      alert("이메일을 입력해주세요");
      return;
    }

    try {
      const response = await fetch("/api/user/findpw", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });

      const data = await response.json();
      alert(
        data.message ||
          "비밀번호 재설정 링크가 이메일로 전송되었습니다 확인해주세요"
      );
    } catch (error) {
      console.error("Error:", error);
      alert("비밀번호 찾기에 실패했습니다");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center py-10 bg-gray-100 w-full">
      <div className="w-full max-w-md bg-white p-3 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-center mb-1">비밀번호 찾기</h2>

        <div className="absolute inset-0 flex flex-col items-center justify-center bg-gray-100 ">
          <img
            src="/images/eonLogo.jpg"
            alt="학교 로고"
            className="w-24 h-24 rounded-full mb-4"
          />
          <div className="flex flex-col items-center justify-center py-10 bg-gray-100 w-full">
            <div className="w-full max-w-md bg-white p-6 rounded-lg shadow-md">
              <h2 className="text-2xl font-bold text-center mb-1">
                비밀번호 찾기
              </h2>

              <form onSubmit={handleFindPassword} className="space-y-3">
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="이메일"
                  className="w-full p-2 border rounded"
                  required
                />

                <button
                  type="submit"
                  className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-800"
                >
                  비밀번호 찾기
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default FindpwPage;
