"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useState } from "react";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";

import Card from "../ui/Card";

export default function QuizStart() {
  const { quizs } = useContext(SocketContext);
  const [step, setStep] = useState<number>(0);

  const next = () => {
    setStep(step + 1);
  };

  return (
    <div>
      {quizs && quizs.length > 0 && (
        <>
          <h1 className="text-3xl">{quizs[step].question}</h1>

          <div className="grid mb-8 border border-gray-200 rounded-lg shadow-sm dark:border-gray-700 md:mb-12 md:grid-cols-2">
            {quizs[step].example.map((quiz) => (
              <Card content={quiz.content} ex={quiz.ex} key={quiz.ex} />
            ))}
          </div>
          {step < quizs.length && (
            <Button
              className="dark:border dark:border-font_primary text-preRg bg-primary"
              type="primary"
              onClick={() => next()}>
              다음
            </Button>
          )}
        </>
      )}
    </div>
  );
}
