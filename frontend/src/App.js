import { useState, createContext, useEffect } from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import AlertModal from "./components/AlertModal";
import ConfirmModal from "./components/ConfirmModal";

export const AuthContext = createContext(null);
export const ModalContext = createContext({
  showModal: () => {},
  showConfirm: () => {},
});

function App() {
  const [userId, setUserId] = useState(() => localStorage.getItem("id"));
  const [userRole, setUserRole] = useState(() => localStorage.getItem("role"));
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [confirmMessage, setConfirmMessage] = useState("");
  const [onConfirmAction, setOnConfirmAction] = useState(() => () => {});
  const [modalType, setModalType] = useState("success");

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

  const showModal = (msg, type = "success") => {
    setModalMessage(msg);
    setModalType(type);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setModalMessage("");
  };

  const showConfirm = ({ message, onConfirm }) => {
    setConfirmMessage(message);
    setOnConfirmAction(() => () => {
      onConfirm?.();
      setIsConfirmOpen(false);
    });
    setIsConfirmOpen(true);
  };

  const cancelConfirm = () => {
    setIsConfirmOpen(false);
    setConfirmMessage("");
  };

  return (
    <AuthContext.Provider value={{ userId, setUserId, userRole, setUserRole }}>
      <ModalContext.Provider value={{ showModal, showConfirm }}>
        <div className="relative">
          <RouterProvider router={root} />
          <AlertModal
            isOpen={isModalOpen}
            message={modalMessage}
            onClose={closeModal}
            type={modalType}
          />

          <ConfirmModal
            isOpen={isConfirmOpen}
            message={confirmMessage}
            onConfirm={onConfirmAction}
            onCancel={cancelConfirm}
          />
        </div>
      </ModalContext.Provider>
    </AuthContext.Provider>
  );
}

export default App;
