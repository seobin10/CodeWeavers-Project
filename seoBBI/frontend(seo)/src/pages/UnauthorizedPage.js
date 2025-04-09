const UnauthorizedPage = () => {
    return (
      <div className="flex flex-col items-center justify-center h-[75vh] text-center">
        <img
          src="/images/eonLogo.jpg"
          alt="로고"
          className="w-24 h-24 rounded-full mb-6 shadow-md"
        />
        <h1 className="text-3xl font-bold text-red-600 mb-3">
          ⛔ 접근이 제한되었습니다
        </h1>
        <p className="text-gray-700 text-lg mb-6">
          해당 페이지에 접근할 수 있는 권한이 없습니다.
        </p>
        <a
          href="/main"
          className="bg-blue-600 text-white px-5 py-2 rounded hover:bg-blue-800 transition"
        >
          메인 페이지로 이동
        </a>
      </div>
    );
  };
  
  export default UnauthorizedPage;