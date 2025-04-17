// 📁 src/routers/studentRouter.js
import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";

const StudentPage = lazy(() => import("../pages/StudentPage"));
const GradePage = lazy(() => import("../pages/GradePage"));
const AllGradePage = lazy(() => import("../pages/AllGradePage"));
const CurrentPage = lazy(() => import("../pages/CurrentPage"));
const EnrollmentPage = lazy(() => import("../pages/EnrollmentPage"));
const HistoryPage = lazy(() => import("../pages/HistoryPage"));
const SchedulePage = lazy(() => import("../pages/SchedulePage"));
const ChangepwPage = lazy(() => import("../components/ChangepwPage"));
const EvaluationPage = lazy(() => import("../pages/EvaluationPage"));
const EvaluationListPage = lazy(() => import("../pages/EvaluationListPage"));
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
    path: "allgrades",
    element: (
      <RoleGuard allowedRoles={["STUDENT"]}>
        <Suspense fallback={<Loading />}>
          <AllGradePage />
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
  {
    path: "evaluationlist",
    element: (
      <RoleGuard allowedRoles={["STUDENT"]}>
        <Suspense fallback={<Loading />}>
          <EvaluationListPage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "evaluation",
    element: (
      <RoleGuard allowedRoles={["STUDENT"]}>
        <Suspense fallback={<Loading />}>
          <EvaluationPage />
        </Suspense>
      </RoleGuard>
    ),
  },
];

export default studentRouter;
