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
      {/* <button className="bg-primary flex mx-auto hover:bg-gray-300 text-secondary py-2 px-4 rounded">방 만들기</button> */}
      <section className="h-screen pt-32 w-full">
        <Test />
        <Timer time={20} />
      </section>
    </>
  );
}
