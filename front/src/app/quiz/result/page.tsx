import QuizResult from "@/components/quiz/QuizResult";
import ChatRoom from "@/components/quiz/ChatRoom";
// import { SocketProvider } from "@/context/SocketProvider";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "QuizResult",
  description: "Shows quiz result",
};

export default function QuizResultPage() {
  return (
    <section className="grid grid-cols-2 h-screen w-full max-w-full">
      <QuizResult />
      <ChatRoom roomId={10} />
    </section>
  );
}
