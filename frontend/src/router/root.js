import { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";
import BasicLayout from "../layouts/BasicLayout";
import memberRouter from "./memberRouter";

//서버에서 데이터를 가져오는 중이다
const Loading = <div>Loading</div>;
const MainPage = lazy(() => import("../pages/MainPage"));
const StudentPage = lazy(() => import("../pages/StudentPage"));
const CoursePage = lazy(() => import("../pages/CoursePage"));
const GradePage = lazy(() => import("../pages/GradePage"));
const EnrollmentPage = lazy(() => import("../pages/EnrollmentPage"));
const SchedulePage = lazy(() => import("../pages/SchedulePage"));

const root = createBrowserRouter([
  {
    path: "",
    element: (
      <Suspense fallback={Loading}>
        <MainPage />
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
    ],
  },

  {
    path: "member",
    children: memberRouter(),
  },
]);

export default root;
