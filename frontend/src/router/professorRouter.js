// 📁 src/routers/professorRouter.js
import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";
import ProfessorListPage from "../pages/Professor/ProfessorEvaluationClassListPage";
import ProfessorEvaluationListPage from "../pages/Professor/ProfessorEvaluationListPage";
import ProfessorEvaluationDataPage from "../pages/Professor/ProfessorEvaluationDataPage";

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
  {
    path: "professor/list",
    element: (
      <RoleGuard allowedRoles={["PROFESSOR"]}>
        <Suspense fallback={<Loading />}>
          <ProfessorListPage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "professor/evaluationlist",
    element: (
      <RoleGuard allowedRoles={["PROFESSOR"]}>
        <Suspense fallback={<Loading />}>
          <ProfessorEvaluationListPage />
        </Suspense>
      </RoleGuard>
    ),
  },
  {
    path: "professor/evaluationdata",
    element: (
      <RoleGuard allowedRoles={["PROFESSOR"]}>
        <Suspense fallback={<Loading />}>
          <ProfessorEvaluationDataPage />
        </Suspense>
      </RoleGuard>
    ),
  },
];

export default professorRouter;
