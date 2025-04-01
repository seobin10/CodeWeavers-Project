import React from "react";
import loadinggif from "../loading.gif";

const Loading = () => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-white bg-opacity-60">
      <h3> 잠시만 기다려주세요 </h3>
      <img src={loadinggif} alt="Loading..." className="w-16 h-16" />
    </div>
  );
};

export default Loading;
