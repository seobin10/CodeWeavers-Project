import { lazy, Suspense } from "react";

const Loading = <div>Loading</div>;
const LoginPage = lazy(() => import("../pages/LoginPage"));
const FindidPage = lazy(() => import("../pages/FindidPage"));
const FindpwPage = lazy(() => import("../pages/FindpwPage"));

const memberRouter = () => {
  return [
    {
      path: "login",
      element: (
        <Suspense fallback={Loading}>
          <LoginPage />
        </Suspense>
      ),
    },
    {
      path: "findid",
      element: (
        <Suspense fallback={Loading}>
          <FindidPage />
        </Suspense>
      ),
    },
    {
      path: "findpw",
      element: (
        <Suspense fallback={Loading}>
          <FindpwPage />
        </Suspense>
      ),
    },
  ];
};

export default memberRouter;
