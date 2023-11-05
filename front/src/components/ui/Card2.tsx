"use client";
import { QuizQuestion } from "@/types/quiz";
import Image from "next/image";
// import logoimg from "../../../public/assets/logo.png"

export default function Card2() {
  return (
    <div className="items-center bg-gray-50 rounded-lg shadow sm:flex dark:bg-gray-800 dark:border-gray-700">
      <h1>1등</h1>
      <Image
        className="w-40 rounded-lg sm:rounded-none sm:rounded-l-lg"
        src="/assets/logo_blue.png"
        alt="인물사진"
        width={500}
        height={300}
      />
      <div className="p-5">
        <h3 className="text-xl font-bold tracking-tight text-gray-900 dark:text-white">윤자현</h3>
        <span className="text-gray-500 dark:text-gray-400">3 / 5</span>
        <p className="mt-3 mb-4 font-light text-gray-500 dark:text-gray-400">5개 중 3개를 맞춤</p>
      </div>
    </div>
  );
}
