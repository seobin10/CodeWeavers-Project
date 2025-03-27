import { useState, createContext, useEffect } from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import AlertModal from "./components/AlertModal";

export const AuthContext = createContext(null);
export const ModalContext = createContext({ showModal: () => {} });

function App() {
  const [userId, setUserId] = useState(() => localStorage.getItem("id"));
  const [userRole, setUserRole] = useState(() => localStorage.getItem("role"));
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState("");

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

  const showModal = (msg) => {
    setModalMessage(msg);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setModalMessage("");
  };

  return (
    <AuthContext.Provider value={{ userId, setUserId, userRole, setUserRole }}>
      <ModalContext.Provider value={{ showModal }}>
        <div className="relative">
          <RouterProvider router={root} />
          <AlertModal
            isOpen={isModalOpen}
            message={modalMessage}
            onClose={closeModal}
          />
        </div>
      </ModalContext.Provider>
    </AuthContext.Provider>
  );
}

export default App;