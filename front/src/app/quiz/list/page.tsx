import QuizList from "@/components/quiz/QuizList";
import { Metadata } from "next";
import Link from "next/link";
import Button from "../../../components/ui/Button";
export const metadata: Metadata = {
  title: "QuizList",
  description: "Show quiz lists",
};

export default function QuizListPage() {
  return (
    <>
      {/* <button className="bg-primary flex mx-auto hover:bg-gray-300 text-secondary py-2 px-4 rounded">방 만들기</button> */}
      <section className="flex items-center justify-center h-screen">
        <QuizList />
      </section>
    </>
  );
}
