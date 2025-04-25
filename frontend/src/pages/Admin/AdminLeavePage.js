import React, { useState, useEffect } from "react";
import AlertModal from "../../components/AlertModal";
import BaseModal from "../../components/BaseModal";
import {
  findStudentName,
  responseLeave,
  seeLeaveList,
} from "../../api/adminLeaveReturnApi";
import { getAllSemesters } from "../../api/adminScheduleApi";

const AdminLeavePage = () => {
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [rejectModalOpen, setRejectModalOpen] = useState(false);
  const [currentRequest, setCurrentRequest] = useState(null);
  const [denialReason, setDenialReason] = useState("");
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("success");

  const fetchLeaveRequests = async () => {
    try {
      setLoading(true);
      const data = await seeLeaveList();
      setLeaveRequests(data);
      setLoading(false);
    } catch (error) {
      console.error("휴학 신청 목록 불러오기 오류:", error);
      showAlert("데이터를 불러오는 데 실패했습니다.", "error");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLeaveRequests();
  }, []);

  const getReasonLabel = (reason) => {
    const map = {
      MILITARY: "군대",
      FAMILY: "가정사",
      ILLNESS: "질병",
      PERSONAL: "개인사정",
      STUDY_ABROAD: "유학",
      OTHER: "기타",
    };
    return map[reason] || reason;
  };

  const getStatusLabel = (status) => {
    const map = {
      PENDING: "대기",
      APPROVED: "승인",
      DENIED: "거절",
    };
    return map[status] || status;
  };

  const handlePrintSemester = async (semesterId) => {
    try {
      const res = await getAllSemesters();
      const currentSemester = res.data.filter(
        (s) => s.semesterId === semesterId
      );
      if (!currentSemester || currentSemester.length === 0) return "-";
      return (
        currentSemester[0].year +
        "년도 " +
        (currentSemester[0].term === "FIRST" ? 1 : 2) +
        "학기"
      );
    } catch (error) {
      console.error("학기 데이터 불러오기 실패", error);
      return "-";
    }
  };

  const handleApprove = async (request) => {
    try {
      await responseLeave(request.leaveId, {
        status: "APPROVED",
        denialReason: null,
      });
      showAlert("휴학 신청이 승인되었습니다.", "success");
      fetchLeaveRequests();
    } catch (error) {
      console.error("승인 오류:", error);
      showAlert("승인 처리 중 오류가 발생했습니다.", "error");
    }
  };

  const openRejectModal = (request) => {
    const nameElement = document.getElementById(request.leaveId);
    const name = nameElement ? nameElement.innerText : "";
    setCurrentRequest({
      ...request,
      studentName: name,
    });

    setDenialReason("");
    setRejectModalOpen(true);
  };

  const handleReject = async (e) => {
    e.preventDefault();
    if (!denialReason.trim()) {
      showAlert("거절 사유를 입력해주세요.", "error");
      return;
    }
    try {
      await responseLeave(currentRequest?.leaveId, {
        status: "DENIED",
        denialReason: denialReason || null,
      });
      setRejectModalOpen(false);
      showAlert("휴학 신청이 거절되었습니다.", "success");
      fetchLeaveRequests();
    } catch (error) {
      console.error("거절 처리 중 오류 발생:", error);
      showAlert("거절 처리 중 오류가 발생했습니다.", "error");
    }
  };

  const showAlert = (message, type) => {
    setAlertMessage(message);
    setAlertType(type);
    setAlertOpen(true);
  };

  const handleStudentName = async (leaveId, type) => {
    if (!leaveId || leaveId.length === 0) return "";
    const data = await findStudentName(leaveId, type);
    return data;
  };

  if (loading)
    return <div className="flex justify-center h-64">로딩 중...</div>;

  return (
    <div className="max-w-8xl mx-auto p-12">
      <div className="bg-white shadow rounded-2xl p-8">
        <h2 className="text-2xl font-semibold mb-6">🔍 휴학 신청 관리</h2>
        <table className="w-full text-center border">
          <thead className="bg-gray-50">
            <tr>
              <th>No.</th>
              <th>학번</th>
              <th>이름</th>
              <th>신청 사유</th>
              <th>상세 사유</th>
              <th>신청일</th>
              <th>복학 예정 학기</th>
              <th>처리 상태</th>
              <th>처리일</th>
              <th>거절 사유</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            {leaveRequests.length > 0 ? (
              leaveRequests.map((req, idx) => (
                <tr key={req.leaveId} className="border-t">
                  <td>{idx + 1}</td>
                  <td>{req.student}</td>
                  <td>
                    {
                      void requestAnimationFrame(() =>
                        handleStudentName(req.leaveId, "leave").then((n) => {
                          const element = document.getElementById(req.leaveId);
                          if (element) element.innerText = n;
                        })
                      )
                    }
                    <span id={req.leaveId}>불러오는 중...</span>
                  </td>
                  <td>{getReasonLabel(req.reason)}</td>
                  <td className="truncate max-w-xs">{req.reasonDetail}</td>
                  <td>{req.requestDate}</td>
                  <td>
                    {
                      void requestAnimationFrame(() =>
                        handlePrintSemester(req.expectedSemester).then((label) => {
                          const element = document.getElementById(
                            `semester-${req.leaveId}`
                          );
                          if (element) element.innerText = label;
                        })
                      )
                    }
                    <span id={`semester-${req.leaveId}`}>불러오는 중...</span>
                  </td>
                  <td>
                    <span
                      className={`px-2 py-1 text-sm rounded ${
                        req.status === "승인"
                          ? "bg-green-100 text-green-700"
                          : req.status === "거절"
                          ? "bg-red-100 text-red-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >
                      {getStatusLabel(req.status)}
                    </span>
                  </td>
                  <td>{req.approvedDate || "-"}</td>
                  <td className="truncate max-w-xs">
                    {req.denialReason || "-"}
                  </td>
                  <td>
                    {req.status === "대기" && (
                      <div className="flex gap-2 justify-center">
                        <button
                          onClick={() => handleApprove(req)}
                          className="bg-green-600 text-white rounded px-3 py-1 text-sm"
                        >
                          승인
                        </button>
                        <button
                          onClick={() => openRejectModal(req)}
                          className="bg-red-600 text-white rounded px-3 py-1 text-sm"
                        >
                          거절
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={11} className="text-gray-400 py-4">
                  신청 내역 없음
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <BaseModal
        isOpen={rejectModalOpen}
        onClose={() => setRejectModalOpen(false)}
      >
        <form onSubmit={handleReject} className="p-4 space-y-4">
          <h2 className="text-xl font-bold">❌ 휴학 신청 거절</h2>
          <p>학번: {currentRequest?.student}</p>
          <p>이름: {currentRequest?.studentName}</p>
          <p>사유: {getReasonLabel(currentRequest?.reason)}</p>
          <textarea
            value={denialReason}
            onChange={(e) => setDenialReason(e.target.value)}
            className="w-full border rounded p-2"
            rows={4}
            placeholder="거절 사유를 입력해주세요"
            required
          />
          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={() => setRejectModalOpen(false)}
              className="bg-gray-300 px-4 py-2 rounded"
            >
              취소
            </button>
            <button
              type="submit"
              className="bg-red-600 text-white px-4 py-2 rounded"
            >
              거절 확인
            </button>
          </div>
        </form>
      </BaseModal>

      <AlertModal
        isOpen={alertOpen}
        message={alertMessage}
        onClose={() => setAlertOpen(false)}
        type={alertType}
      />
    </div>
  );
};

export default AdminLeavePage;
