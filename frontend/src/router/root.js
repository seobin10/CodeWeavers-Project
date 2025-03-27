import { lazy, Suspense } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import memberRouter from "./memberRouter";
import BasicLayout from "../layouts/BasicLayout";
import RoleGuard from "../components/RoleGuard";

const Loading = <div>Loading</div>;
const StudentPage = lazy(() => import("../pages/StudentPage"));
const CoursePage = lazy(() => import("../pages/CoursePage"));
const GradePage = lazy(() => import("../pages/GradePage"));
const EnrollmentPage = lazy(() => import("../pages/EnrollmentPage"));
const HistoryPage = lazy(() => import("../pages/HistoryPage"));
const SchedulePage = lazy(() => import("../pages/SchedulePage"));
const QnaListPage = lazy(() => import("../pages/QnaListPage"));
const QnaDataPage = lazy(() => import("../pages/QnaDataPage"))
const QnaWritePage = lazy(() => import("../pages/QnaWritePage"));
const QnaDeletePage = lazy(() => import("../pages/QnaDeletePage"));
const QnaEditPage = lazy(() => import("../pages/QnaEditPage"));
const UnauthorizedPage = lazy(() => import("../pages/UnauthorizedPage"));
const AdminUserCreatePage = lazy(() => import("../pages/Admin/AdminUserCreatePage"));
const AdminUserListPage = lazy(() => import("../pages/Admin/AdminUserListPage"));

const root = createBrowserRouter([
  {
    path: "/",
    element: <Navigate to="member/login" />,
  },
  {
    path: "/main",
    element: <BasicLayout />,
    children: [
      {
        path: "student",
        element: (
          <Suspense fallback={Loading}>
            <StudentPage />
          </Suspense>
        ),
      },
      {
        path: "courses",
        element: (
          <Suspense fallback={Loading}>
            <CoursePage />
          </Suspense>
        ),
      },
      {
        path: "grades",
        element: (
          <RoleGuard allowedRoles={["STUDENT"]}>
          <Suspense fallback={Loading}>
            <GradePage />
          </Suspense>
          </RoleGuard>
        ),
      },
      {
        path: "enrollment",
        element: (
          <RoleGuard allowedRoles={["STUDENT"]}>
          <Suspense fallback={Loading}>
            <EnrollmentPage />
          </Suspense>
          </RoleGuard>
        ),
      },
      {
        path: "history",
        element: (
          <Suspense fallback={Loading}>
            <HistoryPage />
          </Suspense>
        ),
      },
      {
        path: "schedule",
        element: (
          <RoleGuard allowedRoles={["STUDENT"]}>
          <Suspense fallback={Loading}>
            <SchedulePage />
          </Suspense>
          </RoleGuard>
        ),
      },
      {
        path: "qnalist",
        element: (
          <Suspense fallback={Loading}>
            <QnaListPage />
          </Suspense>
        ),
      },
      {
        path: "qnadata",
        element: (
          <Suspense fallback={Loading}>
            <QnaDataPage />
          </Suspense>
        ),
      },
      {
        path: "qnawrite",
        element: (
          <RoleGuard allowedRoles={["STUDENT", "PROFFESOR"]}>
          <Suspense fallback={Loading}>
            <QnaWritePage />
          </Suspense>
          </RoleGuard>
        ),
      },
      {
        path: "qnadelete",
        element: (
          <Suspense fallback={Loading}>
            <QnaDeletePage />
          </Suspense>
        ),
      },
      {
        path: "qnaedit",
        element: (
          <Suspense fallback={Loading}>
            <QnaEditPage />
          </Suspense>
        ),
      },
      {
        path: "unauthorized",
        element: (
          <Suspense fallback={Loading}>
            <UnauthorizedPage />
          </Suspense>
        ),
      },
      {
        path: "admin/create-user",
        element: (
          <RoleGuard allowedRoles={["ADMIN"]}>
            <Suspense fallback={Loading}>
              <AdminUserCreatePage />
            </Suspense>
          </RoleGuard>
        )
      },
      {
        path: "admin/user-list",
        element: (
          <RoleGuard allowedRoles={["ADMIN"]}>
            <Suspense fallback={Loading}>
              <AdminUserListPage />
            </Suspense>
          </RoleGuard>
        )
      }
    ],
  },
  {
    path: "/member",
    children: memberRouter(),
  },
]);

export default root;
