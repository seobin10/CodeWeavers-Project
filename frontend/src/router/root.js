import { lazy, Suspense } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import memberRouter from "./memberRouter";
import BasicLayout from "../layouts/BasicLayout";

const Loading = <div>Loading</div>;
const StudentPage = lazy(() => import("../pages/StudentPage"));
const CoursePage = lazy(() => import("../pages/CoursePage"));
const GradePage = lazy(() => import("../pages/GradePage"));
const EnrollmentPage = lazy(() => import("../pages/EnrollmentPage"));
const SchedulePage = lazy(() => import("../pages/SchedulePage"));
const QnaListPage = lazy(() => import("../pages/QnaListPage"));
const QnaDataPage = lazy(() => import("../pages/QnaDataPage"))
const QnaWritePage = lazy(() => import("../pages/QnaWritePage"));

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
          <Suspense fallback={Loading}>
            <GradePage />
          </Suspense>
        ),
      },
      {
        path: "enrollment",
        element: (
          <Suspense fallback={Loading}>
            <EnrollmentPage />
          </Suspense>
        ),
      },
      {
        path: "schedule",
        element: (
          <Suspense fallback={Loading}>
            <SchedulePage />
          </Suspense>
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
          <Suspense fallback={Loading}>
            <QnaWritePage />
          </Suspense>
        ),
      },
    ],
  },
  {
    path: "/member",
    children: memberRouter(),
  },
]);

export default root;
