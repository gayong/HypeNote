"use client";

import QuizResult from "@/components/quiz/QuizResult";
import ChatRoom from "@/components/quiz/ChatRoom";
// import { SocketProvider } from "@/context/SocketProvider";
import { Metadata } from "next";
import { Button } from "antd";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import confetti from "canvas-confetti";

const metadata: Metadata = {
  title: "QuizResult",
  description: "Shows quiz result",
};

export default function QuizResultPage() {
  const router = useRouter();

  const outQuiz = () => {
    router.push("/quiz/");
    console.log("퀴즈 나가기");
  };

  useEffect(() => {
    var count = 200;
    var defaults = {
      origin: { y: 0.7 },
    };

    function fire(particleRatio:any, opts:any) {
      confetti({
        ...defaults,
        ...opts,
        particleCount: Math.floor(count * particleRatio),
      });
    }

    fire(0.25, {
      spread: 26,
      startVelocity: 55,
    });
    fire(0.2, {
      spread: 60,
    });
    fire(0.35, {
      spread: 120,
      decay: 0.91,
      scalar: 0.8,
    });
    fire(0.1, {
      spread: 160,
      startVelocity: 25,
      decay: 0.92,
      scalar: 1.2,
    });
    fire(0.1, {
      spread: 160,
      startVelocity: 45,
    });
  }, []);

  return (
    <section className="px-2 pr-6 grid grid-cols-2 h-screen w-full max-w-full items-center">
      <QuizResult />
      <div>
        <ChatRoom roomId={10} />
        <Button
          className="mt-4 dark:border-none dark:border-font_primary font-preRg bg-primary w-full h-14 text-lg "
          type="primary"
          onClick={outQuiz}>
          퀴즈 끝내기
        </Button>
      </div>
    </section>
  );
}
