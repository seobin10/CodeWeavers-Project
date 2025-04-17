// 📁 src/routers/professorRouter.js
import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";

const ProfessorClassPage = lazy(() =>
  import("../pages/Professor/ProfessorClassPage")
);
const ProfessorGradePage = lazy(() =>
  import("../pages/Professor/ProfessorGradePage")
);
const MsgSendPage = lazy(() =>
  import("../pages/Professor/ProfessorMsgSendPage")
);

const professorRouter = [
  {
    path: "professor/classes",
    element: (
      <RoleGuard allowedRoles={["PROFESSOR"]}>
        <Suspense fallback={<Loading />}>
          <ProfessorClassPage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "professor/grades",
    element: (
      <RoleGuard allowedRoles={["PROFESSOR"]}>
        <Suspense fallback={<Loading />}>
          <ProfessorGradePage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "professor/msg",
    element: (
      <RoleGuard allowedRoles={["PROFESSOR"]}>
        <Suspense fallback={<Loading />}>
          <MsgSendPage />
        </Suspense>
      </RoleGuard>
    ),
  },
];

export default professorRouter;
