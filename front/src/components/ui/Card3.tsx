"use client";
import { QuizQuestion } from "@/types/quiz";
import Image from "next/image";
// import logoimg from "../../../public/assets/logo.png"

export default function Card3() {
  return (
    <div className="flex flex-col p-6 mx-auto max-w-lg text-center bg-white rounded-lg border border-gray-100 shadow dark:border-gray-600 xl:p-8 dark:bg-gray-800 dark:text-white">
      <h3 className="mb-4 text-2xl font-semibold">1등</h3>
      <div className="flex-row justify-center items-baseline my-8">
        <Image
          className="w-40 rounded-lg sm:rounded-none sm:rounded-l-lg"
          src="/assets/logo_blue.png"
          alt="인물사진"
          width={500}
          height={300}
        />
        <span className="mr-2 text-5xl font-extrabold">윤자현</span>
      </div>
      <div className="mb-8 space-y-4 text-left">4 / 5</div>
    </div>
  );
}
