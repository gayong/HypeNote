"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useState } from "react";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";

import Quiz from "../ui/Quiz";

export default function QuizStart() {
  const { quizs } = useContext(SocketContext);
  const [step, setStep] = useState<number>(0);

  const next = () => {
    setStep(step + 1);
  };

  const prev = () => {
    setStep(step - 1);
  };

  return (
    <div>
      {quizs && quizs.length > 0 && (
        <>
          <h1 className="text-3xl">{quizs[step].question}</h1>

          {/* <Quiz /> */}
          {step < quizs.length && (
            <Button
              className="dark:border dark:border-font_primary text-preRg bg-primary"
              type="primary"
              onClick={() => next()}>
              다음
            </Button>
          )}
          {step != 0 && (
            <Button
              className="dark:border dark:border-font_primary text-preRg bg-white"
              type="primary"
              onClick={() => prev()}>
              이전
            </Button>
          )}
        </>
      )}
    </div>
  );
}
