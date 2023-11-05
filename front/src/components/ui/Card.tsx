"use client";
import { QuizQuestion } from "@/types/quiz";

export default function Card(props: QuizQuestion) {
  return (
    <figure className="flex flex-col items-center justify-center p-8 text-center border-b border-gray-200 rounded-t-lg md:rounded-t-none md:rounded-tl-lg md:border-r hover:bg-primary hover:text-font_primary">
      <blockquote className="max-w-2xl mx-auto mb-4 text-gray-500 lg:mb-8">
        <h3 className="text-lg ">{props.content}</h3>
      </blockquote>
    </figure>
  );
}
