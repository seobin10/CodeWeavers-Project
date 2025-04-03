// 📁 src/routers/index.js
import { Navigate, createBrowserRouter } from "react-router-dom";
import BasicLayout from "../layouts/BasicLayout";
import memberRouter from "./memberRouter";
import studentRouter from "./studentRouter";
import adminRouter from "./adminRouter";
import professorRouter from "./professorRouter";
import qnaRouter from "./qnaRouter";
import UnauthorizedPage from "../pages/UnauthorizedPage";
import Loading from "../components/Loading";
import { Suspense, lazy } from "react";

const root = createBrowserRouter([
  { path: "/", element: <Navigate to="/member/login" /> },
  {
    path: "/main",
    element: <BasicLayout />,
    children: [
      ...studentRouter,
      ...adminRouter,
      ...professorRouter,
      ...qnaRouter,
      {
        path: "unauthorized",
        element: (
          <Suspense fallback={<Loading />}>
            <UnauthorizedPage />
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
