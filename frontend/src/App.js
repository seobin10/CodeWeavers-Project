import { useState, createContext, useEffect } from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";

export const AuthContext = createContext(null);

function App() {
  const [userId, setUserId] = useState(() => localStorage.getItem("id"));
  const [userRole, setUserRole] = useState(() => localStorage.getItem("role"));

  useEffect(() => {
    if (userId) {
      localStorage.setItem("id", userId);
    } else {
      localStorage.removeItem("id");
    }
  }, [userId]);

  useEffect(() => {
    if (userRole) {
      localStorage.setItem("role", userRole);
    } else {
      localStorage.removeItem("role");
    }
  }, [userRole]);

  return (
    <AuthContext.Provider value={{ userId, setUserId, userRole, setUserRole }}>
      <RouterProvider router={root} />
    </AuthContext.Provider>
  );
}

export default App;