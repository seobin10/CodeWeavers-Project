import { useState, createContext, useEffect } from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";

export const AuthContext = createContext(null);

function App() {
  const [userId, setUserId] = useState(() => {
    return localStorage.getItem("id"); 
  });

  useEffect(() => {
    if (userId) {
      localStorage.setItem("id", userId);
    } else {
      localStorage.removeItem("id");
    }
  }, [userId]);

  return (
    <AuthContext.Provider value={{ userId, setUserId }}>
      <RouterProvider router={root} />
    </AuthContext.Provider>
  );
}

export default App;
