import { useState, useEffect, createContext } from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import AlertModal from "./components/AlertModal";

export const AuthContext = createContext(null);
export const ModalContext = createContext({ showModal: () => {} });

function App() {
  const [userId, setUserId] = useState(() => localStorage.getItem("id"));
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalInfo, setModalInfo] = useState({ message: "", type: "success" });

  const showModal = (message, type = "error") => {
    setModalInfo({ message, type });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setModalInfo({ message: "", type: "success" });
  };

  useEffect(() => {
    if (userId) {
      localStorage.setItem("id", userId);
    } else {
      localStorage.removeItem("id");
    }
  }, [userId]);

  return (
    <AuthContext.Provider value={{ userId, setUserId }}>
      <ModalContext.Provider value={{ showModal }}>
        <div className="relative">
          <RouterProvider router={root} />
          <AlertModal
            isOpen={isModalOpen}
            message={modalInfo.message}
            type={modalInfo.type}
            onClose={closeModal}
          />
        </div>
      </ModalContext.Provider>
    </AuthContext.Provider>
  );
}

export default App;
