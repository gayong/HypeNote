import QuizMain from "@/components/quiz/QuizMain";
// import { SocketProvider } from "@/context/SocketProvider";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Quiz",
  description: "Quiz MainPage",
};

export default function QuizPage() {
  return (
    <section className="flex items-center justify-center h-screen">
      <QuizMain />
    </section>
  );
}
