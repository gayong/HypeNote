import QuizList from "@/components/quiz/QuizList";
import { Metadata } from "next";
import Link from "next/link";
import Button from "../../../components/ui/Button";
import { useContext } from "react";
import { SocketContext } from "@/context/SocketProvider";
export const metadata: Metadata = {
  title: "QuizList",
  description: "Show quiz lists",
};

export default function QuizListPage() {
  const handleCreateRoom = () => {
    console.log("방만들기");
  };

  return (
    <>
      <h1 className="text-3xl font-bold mb-5 ">퀴즈 리스트를 보여주는 페이지</h1>
      <div className="mx-auto">
        <Link href="/quiz/maker">
          <Button text="방 만들기" onClick={handleCreateRoom}></Button>
        </Link>
      </div>
      <section className="flex items-center justify-center h-screen">
        <QuizList />
      </section>
    </>
  );
}
