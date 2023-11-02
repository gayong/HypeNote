"use client";

import { useTheme } from "next-themes";
import { useEffect, useState } from "react";
import { BiSolidSun } from "react-icons/bi";
import { BsFillMoonFill } from "react-icons/bs";

export default function DarkModeBtn() {
  const [currentTheme, setCurrentTheme] = useState<"light" | "dark" | null>(null);
  const { setTheme } = useTheme();

  useEffect(() => {
    if (typeof window !== "undefined") {
      const localTheme = window.localStorage.getItem("theme");
      setCurrentTheme(localTheme === "light" ? "light" : "dark");
    }
  }, []);

  useEffect(() => {
    if (currentTheme) {
      setTheme(currentTheme);
      window.localStorage.setItem("theme", currentTheme);
    }
  }, [currentTheme]);

  const changeTheme = () => {
    setCurrentTheme((current) => (current === "dark" ? "light" : "dark"));
  };

  return (
    <div>
      {currentTheme === "light" ? (
        <div>
          <button
            title="다크모드로 변경"
            onClick={changeTheme}
            className="hover:bg-font_primary hover:bg-opacity-30 justify-start items-center flex bg-transparent w-[45px] h-[25px] border-[1.6px] hover:bg-gray-300 text-secondary rounded-3xl">
            <BiSolidSun className="ml-1.5 mb-[0.8px] text-[18px] rounded-xl " />
          </button>
        </div>
      ) : (
        <div>
          <button
            title="라이트모드로 변경"
            onClick={changeTheme}
            className="hover:bg-font_primary hover:bg-opacity-30 justify-end items-center flex bg-transparent w-[45px] h-[25px] border-[1.6px] hover:bg-gray-300 text-secondary rounded-3xl">
            <BsFillMoonFill className="mr-1.5 mb-[1.2px] text-[12px]" />
          </button>
        </div>
      )}
    </div>
  );
}
