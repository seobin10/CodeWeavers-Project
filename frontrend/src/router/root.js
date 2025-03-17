import { lazy, Suspense } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import BasicLayout from "../layouts/BasicLayout";

const Loading = <div>Loading....</div>;
const LoginPage = lazy(() => import("../pages/LoginPage"));
const StudentPage = lazy(() => import("../pages/StudentPage"));
const CoursePage = lazy(() => import("../pages/CoursePage"));
const GradePage = lazy(() => import("../pages/GradePage"));

const root = createBrowserRouter([
  {
    path: "/",
    element: <Navigate to="/login" />,
  },
  {
    path: "/login",
    element: (
      <Suspense fallback={Loading}>
        <LoginPage />
      </Suspense>
    ),
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
    ],
  },
]);

export default root;
