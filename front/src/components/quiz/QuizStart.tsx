"use client";
import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect } from "react";
import Card from "../ui/Card";

export default function QuizStart() {
  const { quizs } = useContext(SocketContext);

  return (
    <div>
      {quizs && quizs.length > 0 && (
        <>
          <h1>{quizs[0].question}</h1>

          <div className="grid mb-8 border border-gray-200 rounded-lg shadow-sm dark:border-gray-700 md:mb-12 md:grid-cols-2">
            {quizs[0].example.map((quiz) => (
              <Card content={quiz.content} ex={quiz.ex} key={quiz.ex} />
            ))}
          </div>
        </>
      )}
    </div>
  );
}
