// 📁 src/routers/studentRouter.js
import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";

const StudentPage = lazy(() => import("../pages/StudentPage"));
const GradePage = lazy(() => import("../pages/GradePage"));
const CurrentPage = lazy(() => import("../pages/CurrentPage"));
const EnrollmentPage = lazy(() => import("../pages/EnrollmentPage"));
const HistoryPage = lazy(() => import("../pages/HistoryPage"));
const SchedulePage = lazy(() => import("../pages/SchedulePage"));
const ChangepwPage =  lazy(() => import("../components/ChangepwPage"));

const studentRouter = [
  {
    path: "student",
    element: (
      <Suspense fallback={<Loading />}>
        <StudentPage />
      </Suspense>
    ),
  },
  {
    path: "grades",
    element: (
      <RoleGuard allowedRoles={["STUDENT"]}>
        <Suspense fallback={<Loading />}>
          <GradePage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "currentgrades",
    element: (
      <Suspense fallback={<Loading />}>
        <CurrentPage />
      </Suspense>
    ),
  },
  {
    path: "enrollment",
    element: (
      <RoleGuard allowedRoles={["STUDENT"]}>
        <Suspense fallback={<Loading />}>
          <EnrollmentPage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "history",
    element: (
      <Suspense fallback={<Loading />}>
        <HistoryPage />
      </Suspense>
    ),
  },
  {
    path: "schedule",
    element: (
      <RoleGuard allowedRoles={["STUDENT"]}>
        <Suspense fallback={<Loading />}>
          <SchedulePage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "password",
    element: <ChangepwPage />,
  },
];

export default studentRouter;
