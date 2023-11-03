import QuizList from "@/components/quiz/QuizList";
import { Metadata } from "next";
import Link from "next/link";
import Button from "../../../components/ui/Button";
import { useContext } from "react";
import { SocketContext } from "@/context/SocketProvider";

export const metadata: Metadata = {
  title: "QuizRoom",
  description: "Show quiz lists",
};

export default function QuizRoomPage() {
  return (
    <>
      {/* <button className="bg-primary flex mx-auto hover:bg-gray-300 text-secondary py-2 px-4 rounded">방 만들기</button> */}
      <section className="flex items-center justify-center ">
        <QuizList />
      </section>
    </>
  );
}
