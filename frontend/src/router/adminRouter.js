// 📁 src/routers/adminRouter.js
import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";

const AdminUserListPage = lazy(() =>
  import("../pages/Admin/AdminUserListPage")
);
const AdminDashboardPage = lazy(() =>
  import("../pages/Admin/AdminDashboardPage")
);

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
          <AdminDashboardPage />
        </Suspense>
      </RoleGuard>
    ),
  },
];

export default adminRouter;
