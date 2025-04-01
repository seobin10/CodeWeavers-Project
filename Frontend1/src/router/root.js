import { lazy, Suspense } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import memberRouter from "./memberRouter";
import BasicLayout from "../layouts/BasicLayout";
import Loading from "../components/Loading";
import RoleGuard from "../components/RoleGuard";

const StudentPage = lazy(() => import("../pages/StudentPage"));
const CoursePage = lazy(() => import("../pages/CoursePage"));
const GradePage = lazy(() => import("../pages/GradePage"));
const CurrentPage = lazy(() => import("../pages/CurrentPage"));
const EnrollmentPage = lazy(() => import("../pages/EnrollmentPage"));
const HistoryPage = lazy(() => import("../pages/HistoryPage"));
const SchedulePage = lazy(() => import("../pages/SchedulePage"));
const QnaListPage = lazy(() => import("../pages/QnaListPage"));
const QnaDataPage = lazy(() => import("../pages/QnaDataPage"));
const QnaWritePage = lazy(() => import("../pages/QnaWritePage"));
const QnaDeletePage = lazy(() => import("../pages/QnaDeletePage"));
const QnaEditPage = lazy(() => import("../pages/QnaEditPage"));
const UnauthorizedPage = lazy(() => import("../pages/UnauthorizedPage"));
const AdminUserCreatePage = lazy(() => import("../pages/Admin/AdminUserCreatePage"));
const AdminUserListPage = lazy(() => import("../pages/Admin/AdminUserListPage"));
const AnsWritePage = lazy(() => import("../pages/Admin/QnaAnswerWritePage"));
const AnsDeletePage = lazy (()=> import("../pages/Admin/QnaAnswerDeletePage"));

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
          <Suspense fallback={<Loading />}>
            <StudentPage />
          </Suspense>
        ),
      },
      {
        path: "courses",
        element: (
          <Suspense fallback={<Loading />}>
            <CoursePage />
          </Suspense>
        ),
      },
      {
        path: "grades",
        element: (
          <Suspense fallback={<Loading />}>
            <GradePage />
          </Suspense>
        ),
      },
      {
        path: "currentgrades", // 현재학기성적조회
        element: (
          <Suspense fallback={<Loading />}>
            <CurrentPage />
          </Suspense>
        ),
      },
      {
        path: "enrollment",
        element: (
          <Suspense fallback={<Loading />}>
            <EnrollmentPage />
          </Suspense>
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
          <Suspense fallback={<Loading />}>
            <SchedulePage />
          </Suspense>
        ),
      },
      {
        path: "qnalist",
        element: (
          <Suspense fallback={<Loading />}>
            <QnaListPage />
          </Suspense>
        ),
      },
      {
        path: "qnadata",
        element: (
          <Suspense fallback={<Loading />}>
            <QnaDataPage />
          </Suspense>
        ),
      },
      {
        path: "qnawrite",
        element: (
          <Suspense fallback={<Loading />}>
            <QnaWritePage />
          </Suspense>
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
        path: "ansdelete",
        element: (
          <Suspense fallback={Loading}>
            <AnsDeletePage />
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
