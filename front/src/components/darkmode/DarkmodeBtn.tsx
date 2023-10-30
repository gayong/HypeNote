"use client";

import { useTheme } from "next-themes";
import { useEffect, useState } from "react";
import { BiSolidSun } from "react-icons/bi";
import { BsFillMoonStarsFill } from "react-icons/bs";

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
            className="hover:bg-font_primary hover:bg-opacity-30 justify-center items-center flex bg-transparent w-[60px] h-[30px] border-[1.6px] mx-auto hover:bg-gray-300 text-secondary rounded-3xl">
            <BiSolidSun className="text-[20px]" />
          </button>
        </div>
      ) : (
        <div>
          <button
            title="라이트모드로 변경"
            onClick={changeTheme}
            className="hover:bg-font_primary hover:bg-opacity-30 justify-center items-center flex bg-transparent w-[60px] h-[30px] border-[1.6px] mx-auto hover:bg-gray-300 text-secondary rounded-3xl">
            <BsFillMoonStarsFill className="text-[14px]" />
          </button>
        </div>
      )}
    </div>
  );
}
