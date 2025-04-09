import { lazy, Suspense } from "react";
import RoleGuard from "../components/RoleGuard";
import Loading from "../components/Loading";

const MsgSendPage = lazy(()=> import("../pages/Professor/MsgSendPage"));
const msgRouter = [

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

export default msgRouter;
