import ChatRoom from "@/components/quiz/ChatRoom";
import Test from "@/components/quiz/test2";
import Timer from "@/components/ui/Timer";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "QuizRoom",
  description: "Show quiz lists",
};

export default function TestPage() {
  return (
    <>
      <section className="flex items-center justify-center ">
        <section className="px-2 pr-6 grid grid-cols-2 h-screen w-full max-w-full items-center">
          <Test />

          <div className="pl-2">
            <ChatRoom roomId={10} height={80} />
          </div>
        </section>
      </section>
    </>
  );
}
