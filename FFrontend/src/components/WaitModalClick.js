// 모달 입력 대기 (클릭될 때까지 다음 코드의 동작을 멈추도록 함)
export const WaitModalClick = () =>
    new Promise((resolve) => {
      const handler = () => {
        document.removeEventListener("click", handleClick);
        resolve();
      };
  
      const handleClick = (e) => {
        if (e.target.textContent === "확인") handler();
      };
  
      document.addEventListener("click", handleClick, { once: true });
    });
  