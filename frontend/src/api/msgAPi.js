import axios from "axios";
import { getAuthHeader } from "../util/authHeader";

const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/professor/msg`;

// 메시지 전송 API
export const sendMessage = async (data) => {
  const formData = new URLSearchParams();
  formData.append("to", data.to);
  formData.append("text", data.text);

  const config = {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      ...getAuthHeader(),
    },
  };

  const res = await axios.post(`${prefix}/send`, formData, config);
  return res.data;
};
