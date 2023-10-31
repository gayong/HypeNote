"use client";

import Link from "next/link";
import { useEffect } from "react";

export default function QuizMain() {
  return (
    <div>
      {/* <h1 className="text-3xl font-bold">여긴 퀴즈페이지</h1> */}
      <div className="mx-auto max-w-2xl text-center">
        <h2 className="text-primary text-base font-['preBd'] ">Quiz</h2>
        <p className="text-[13px] text-lg leading-8">
          공부한 내용을 퀴즈로 풀어보며 복습해보세요. <br /> 공부한 노트 페이지들을 퀴즈 범위로 지정하고, 혼자 풀거나
          친구들을 초대해 대결해보세요.
        </p>
      </div>
      <div className="flex justify-between mt-10">
        <Link href="/quiz/maker">
          <div
            className="bg-font_primary rounded-3xl mr-32 flex text-center justify-center align-center items-center h-[25rem] w-[20rem] shadow-lg
        hover:-translate-y-1 hover:scale-105 duration-300 cursor-pointer">
            혼자풀기
          </div>
        </Link>
        <Link href="/quiz/list">
          <div
            className="bg-font_primary rounded-3xl flex text-center justify-center align-middle items-center h-[25rem] w-[20rem] shadow-lg
        hover:-translate-y-1 hover:scale-105 duration-300 cursor-pointer">
            같이풀기
          </div>
        </Link>
      </div>
    </div>
  );
}
