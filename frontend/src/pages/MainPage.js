import React from "react";
import { Link } from "react-router-dom";

const MainPage = () => {
  return (
    <div>
      <nav>
        <h1
          style={{ color: "beige", backgroundColor: "gray", padding: "25px" }}
        >
          <div>
            메뉴&nbsp;&nbsp;&nbsp;
            <Link to={`/user/ADMIN1111`}>기본 정보</Link> <Link>2</Link> <Link>3</Link>{" "}
            <Link>4</Link>
          </div>
        </h1>
      </nav>
      <article
        style={{ backgroundColor: "burlywood", height: "50vw" }}
      ></article>
    </div>
  );
};

export default MainPage;
