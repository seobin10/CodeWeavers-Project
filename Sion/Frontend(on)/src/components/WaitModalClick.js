// 모달 입력 대기 (엔터 혹은 클릭될 때까지 다음 코드의 동작을 멈추도록 함)
export const WaitModalClick = () =>
    new Promise((resolve) => {
      const handler = () => {
        // window.removeEventListener("keydown", handleKey);
        document.removeEventListener("click", handleClick);
        resolve();
      };
  
      // const handleKey = (e) => {
      //   if (e.key === "Enter") handler();
      // };
  
      const handleClick = (e) => {
        if (e.target.textContent === "확인") handler();
      };
  
      // window.addEventListener("keydown", handleKey, { once: true });
      document.addEventListener("click", handleClick, { once: true });
    });
  