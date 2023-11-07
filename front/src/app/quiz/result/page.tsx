"use client";

import QuizResult from "@/components/quiz/QuizResult";
import ChatRoom from "@/components/quiz/ChatRoom";
// import { SocketProvider } from "@/context/SocketProvider";
import { Metadata } from "next";
import { Button } from "antd";
import { useRouter } from "next/navigation";

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
