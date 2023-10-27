"use client";

import { useTheme } from "next-themes";
import { useEffect, useState } from "react";

export default function DarkModeBtn() {
  const { setTheme } = useTheme();
  const [currentTheme, setCurrentTheme] = useState(false);
  // 나중에 사용자 정보 받아와서 처리하기
  // true => light
  // false => dark

  useEffect(() => {
    setTheme(currentTheme ? "light" : "dark");
  }, [currentTheme]);

  const changeTheme = () => {
    setCurrentTheme(!currentTheme);
  };

  return (
    <div>
      <button
        onClick={() => {
          changeTheme();
        }}
      >
        {currentTheme ? <div>다크 모드 버튼</div> : <div>라이트 모드 버튼</div>}
      </button>
    </div>
  );
}
