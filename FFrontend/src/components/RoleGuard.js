import { useContext } from "react";
import { AuthContext } from "../App";
import { Navigate } from "react-router-dom";

const RoleGuard = ({ allowedRoles, children }) => {
  const { userRole } = useContext(AuthContext);

  if (!allowedRoles.includes(userRole)) {
    return <Navigate to="/main/unauthorized" replace />;
  }

  return children;
};

export default RoleGuard;