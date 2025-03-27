import React from "react";
import AdminUserCreatePage from "../pages/Admin/AdminUserCreatePage";

const AdminUserModal = ({ isOpen, onClose, onSuccess }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg w-[600px] max-h-[95vh] overflow-y-auto shadow-xl relative">
        <button
           onClick={() => {
            onClose();
            onSuccess(); 
          }}
          className="absolute top-3 right-3 text-gray-600 hover:text-black text-xl"
        >
          ✖
        </button>
        <AdminUserCreatePage />
      </div>
    </div>
  );
};

export default AdminUserModal;
