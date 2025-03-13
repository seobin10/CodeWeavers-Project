import { useState, createContext } from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";

export const AuthContext = createContext(null);

function App() {
  const [userId, setUserId] = useState(null); // 로그인 상태 관리

  return (
    <AuthContext.Provider value={{ userId, setUserId }}>
      <RouterProvider router={root} />
    </AuthContext.Provider>
  );
}

export default App;
