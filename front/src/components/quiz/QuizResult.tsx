"use client";
import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect } from "react";
import Card3 from "../ui/Card3";

import { Button } from "antd";

export default function QuizResult() {
  const { quizResults } = useContext(SocketContext);
  const handleGetQuiz = () => {
    console.log("틀린문제를 공개해");
  };

  return (
    <section className="bg-white dark:bg-gray-900">
      <div className="py-8 px-4 mx-auto max-w-screen-xl lg:py-16 lg:px-6">
        <div className="mx-auto max-w-screen-md text-center mb-8 lg:mb-12">
          <h2 className="mb-4 text-4xl tracking-tight font-extrabold text-gray-900 dark:text-white">퀴즈 결과</h2>
          <p className="mb-5 font-light text-gray-500 sm:text-xl dark:text-gray-400">퀴즈 결과를 공개합니다 !</p>
        </div>
        <div className="space-y-8 lg:grid lg:grid-cols-3 sm:gap-6 xl:gap-10 lg:space-y-0">
          <Card3 />
          <Card3 />
          <Card3 />
        </div>
      </div>
      <Button
        className="dark:border dark:border-font_primary"
        style={{ fontFamily: "preRg", backgroundColor: "#2946A2" }}
        type="primary"
        onClick={() => handleGetQuiz()}>
        틀린문제보기
      </Button>
    </section>
  );
}
