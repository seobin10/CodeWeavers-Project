import React from "react";

const PageComponent = ({ currentPage, totalPage, onPageChange }) => {
  const pages = Array.from({ length: totalPage }, (_, i) => i + 1);

  return (
    <div className="flex justify-center mt-4 space-x-2">
      {pages.map((page) => (
        <button
          key={page}
          onClick={() => onPageChange(Number(page))}
          className={`px-3 py-1 border rounded ${
            page === currentPage ? "bg-blue-500 text-white" : "bg-gray-200"
          }`}
        >
          {page}
        </button>
      ))}
    </div>
  );
};

export default PageComponent;
