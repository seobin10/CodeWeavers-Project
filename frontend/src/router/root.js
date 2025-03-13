import { Suspense, lazy } from "react";
import { createBrowserRouter } from "react-router-dom";

const Loading = <div>로딩 중...</div>
const Main = lazy(()=> import("../pages/MainPage"));
const Student = lazy(()=> import("../pages/StudentMyPage"));
const root = createBrowserRouter([

    {
        path : "",
        element : <Suspense fallback={Loading}><Main/></Suspense>
    },
    {
        path : "user/:userId",
        element : <Suspense fallback={Loading}><Student/></Suspense>
    }
])
export default root;