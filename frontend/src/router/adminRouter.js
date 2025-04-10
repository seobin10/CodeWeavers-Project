import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";

const AdminUserListPage = lazy(() => import("../pages/Admin/AdminUserListPage"));
const AdminSchedulePage = lazy(() => import("../pages/Admin/AdminSchedulePage"));
const AdminGradePage = lazy(() => import("../pages/Admin/AdminGradePage")); 

const adminRouter = [
  {
    path: "admin/user-list",
    element: (
      <RoleGuard allowedRoles={["ADMIN"]}>
        <Suspense fallback={<Loading />}>
          <AdminUserListPage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "users",
    element: (
      <RoleGuard allowedRoles={["ADMIN"]}>
        <Suspense fallback={<Loading />}>
          <AdminSchedulePage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "grades", 
    element: (
      <RoleGuard allowedRoles={["ADMIN"]}>
        <Suspense fallback={<Loading />}>
          <AdminGradePage />
        </Suspense>
      </RoleGuard>
    ),
  },
];

export default adminRouter;