"use client";

import { TreeSelect, Select, Button, message, Steps, theme } from "antd";
import { useState } from "react";
import Quiz from "@/components/ui/Quiz";
import { useSendQuizAnswer } from "@/hooks/useSendQuizAnswer";
import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";

const quizs = [
  {
    id: 1,
    question: "IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?",
    example: [
      {
        ex: "1",
        content: "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다.",
      },
      {
        ex: "2",
        content: "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.",
      },
      {
        ex: "3",
        content: "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다.",
      },
      {
        ex: "4",
        content: "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다.",
      },
    ],
    answer: "2",
    commentary: "해설1",
  },
  {
    id: 2,
    question: "TCP/IP는 어떤 두 가지 프로토콜로 구성되어 있으며, 어떤 역할을 각각 수행하고 있는가?",
    example: [
      {
        ex: "1",
        content: "TCP가 데이터의 추적을 처리하고, IP가 데이터의 배달을 담당한다.",
      },
      {
        ex: "2",
        content: "IP가 데이터의 추적을 처리하고, TCP가 데이터의 배달을 담당한다.",
      },
      {
        ex: "3",
        content: "TCP와 IP가 모두 데이터의 재조합을 처리한다.",
      },
      {
        ex: "4",
        content: "TCP와 IP가 모두 데이터의 손실 여부를 확인한다.",
      },
    ],
    answer: "1",
    commentary: "해설2",
  },
  {
    id: 3,
    question: "IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?",
    example: [
      {
        ex: "1",
        content: "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다.",
      },
      {
        ex: "2",
        content: "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.",
      },
      {
        ex: "3",
        content: "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다.",
      },
      {
        ex: "4",
        content: "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다.",
      },
    ],
    answer: "2",
    commentary: "해설1",
  },
  {
    id: 4,
    question: "IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?",
    example: [
      {
        ex: "1",
        content: "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다.",
      },
      {
        ex: "2",
        content: "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.",
      },
      {
        ex: "3",
        content: "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다.",
      },
      {
        ex: "4",
        content: "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다.",
      },
    ],
    answer: "2",
    commentary: "해설1",
  },
  {
    id: 5,
    question: "IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?",
    example: [
      {
        ex: "1",
        content: "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다.",
      },
      {
        ex: "2",
        content: "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.",
      },
      {
        ex: "3",
        content: "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다.",
      },
      {
        ex: "4",
        content: "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다.",
      },
    ],
    answer: "2",
    commentary: "해설1",
  },
  {
    id: 6,
    question: "IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?",
    example: [
      {
        ex: "1",
        content: "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다.",
      },
      {
        ex: "2",
        content: "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.",
      },
      {
        ex: "3",
        content: "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다.",
      },
      {
        ex: "4",
        content: "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다.",
      },
    ],
    answer: "2",
    commentary: "해설1",
  },
];

export default function Test() {
  const [step, setStep] = useState<number>(0);
  const [answers, setAnswers] = useState<{ [key: string]: string }>({});
  const { sendQuizAnswer } = useSendQuizAnswer();
  const [user] = useAtom(userAtom);
  const handleAnswerChange = (questionId: string, answer: string) => {
    setAnswers({
      ...answers,
      [questionId]: answer,
    });
    console.log(answers);
  };

  const next = () => {
    setStep(step + 1);
  };

  const prev = () => {
    setStep(step - 1);
  };

  const submitAnswer = () => {
    console.log("답제출");
    sendQuizAnswer.mutate({
      roomId: 122,
      userId: 2,
      answers: answers,
    });
  };

  const items = quizs.map((quiz) => ({
    key: quiz.id,
    title: <div className="dark:text-font_primary text-md font-semibold">{quiz.id}번</div>,
  }));

  // 모든 퀴즈 질문에 "모르겠어요" 옵션 추가
  const quizsWithExtraOption = quizs.map((quiz) => ({
    ...quiz,
    example: [...quiz.example, { ex: "0", content: "모르겠어요." }],
  }));

  return (
    <div className="my-24 flex-cols items-center justify-center h-screen mx-16 w-full">
      <Steps
        status="process"
        type="navigation"
        percent={((step + 1) / quizs.length) * 100}
        size="small"
        className="site-navigation-steps"
        current={step}
        items={items}
      />

      <h1 className="text-3xl font-preBd mt-11">
        <span className="text-4xl">{step + 1}. </span>

        {quizsWithExtraOption[step].question}
      </h1>

      <div>
        <Quiz
          example={quizsWithExtraOption[step].example}
          questionId={quizsWithExtraOption[step].id}
          onAnswerChange={handleAnswerChange}
          selectedAnswer={answers[quizsWithExtraOption[step].id]}
        />
      </div>

      <div className="flex justify-between">
        {step != 0 ? (
          <Button className=" dark:border-font_primary font-preRg bg-white" type="primary" onClick={() => prev()}>
            이전
          </Button>
        ) : (
          <br />
        )}
        {step < quizsWithExtraOption.length - 1 ? (
          <Button className="font-preRg bg-primary" type="primary" onClick={() => next()}>
            다음
          </Button>
        ) : (
          <Button className=" font-preRg bg-primary" type="primary" onClick={() => submitAnswer()}>
            답안 제출
          </Button>
        )}
      </div>
    </div>
  );
}
