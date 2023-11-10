"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";
import { useSendQuizAnswer } from "@/hooks/useSendQuizAnswer";
import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";

import Quiz from "../ui/Quiz";
import { useRouter } from "next/navigation";

interface QuizRoomProps {
  roomId: number;
  setSubmit: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function QuizStart(props: QuizRoomProps) {
  const [step, setStep] = useState<number>(0);
  const [answers, setAnswers] = useState<{ [key: string]: string }>({});

  const { sendQuizAnswer } = useSendQuizAnswer();
  const { quizs } = useContext(SocketContext);

  const [user] = useAtom(userAtom);
  const router = useRouter();

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
      roomId: props.roomId,
      userId: user.userPk,
      answers: answers,
    });
  };

  useEffect(() => {
    if (sendQuizAnswer.isSuccess) {
      message.success("답안을 제출했어요.");
      // 결과 페이지 이동
      // router.replace(`/quiz/room/${props.roomId}/result`);
      props.setSubmit(true);
    } else if (sendQuizAnswer.isError) {
      message.error("답안 제출 에러가 발생했어요. 잠시 후 다시 제출해주세요.");
    }
  }, [sendQuizAnswer]);

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
    <>
      {quizs && quizs.length > 0 && (
        <>
          <div className="my-24 flex-cols items-center justify-center h-screen mx-16 w-full pt-10 ">
            <Steps
              status="process"
              type="navigation"
              percent={((step + 1) / quizs.length) * 100}
              size="small"
              className="site-navigation-steps dark:text-font_primary"
              current={step}
              items={items}
            />

            <h1 className="text-3xl font-preBd mt-11 h-12">
              <span className="text-4xl">{step + 1}. </span>

              {quizsWithExtraOption[step].question}
            </h1>
            <div>
              <Quiz
                example={quizsWithExtraOption[step].example}
                questionId={quizsWithExtraOption[step].id}
                onAnswerChange={handleAnswerChange}
                selectedAnswer={answers[quizsWithExtraOption[step].id] || ""}
              />
            </div>

            <div className="flex justify-center mt-10">
              {step != 0 ? (
                <Button
                  className=" dark:border-font_primary font-preRg bg-line_primary"
                  type="primary"
                  onClick={() => prev()}>
                  이전
                </Button>
              ) : (
                <br />
              )}
              {step < quizsWithExtraOption.length - 1 ? (
                <Button className="font-preRg bg-primary ml-3 pl-3" type="primary" onClick={() => next()}>
                  다음
                </Button>
              ) : (
                <Button className=" font-preRg bg-primary" type="primary" onClick={() => submitAnswer()}>
                  답안 제출
                </Button>
              )}
            </div>
          </div>
        </>
      )}
    </>
  );
}
