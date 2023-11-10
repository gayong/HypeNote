import React from "react";
import { QuizQuestion } from "@/types/quiz";
export default function Quiz({
  example,
  questionId,
  onAnswerChange,
  selectedAnswer,
}: {
  example: QuizQuestion[];
  questionId: number;
  onAnswerChange: (questionId: string, answer: string) => void;
  selectedAnswer: string;
}) {
  const handleAnswerChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onAnswerChange(questionId.toString(), event.target.value);
  };

  return (
    <div className="font-preRg pt-6">
      <ul className="w-full text-sm font-medium">
        {example.map((item) => (
          <li key={item.ex} className="w-full">
            <div className="flex items-center pl-3">
              <input
                id={`quiz-radio-${questionId}-${item.ex}`}
                type="radio"
                name={`quiz-${questionId}`}
                value={item.ex}
                checked={selectedAnswer === item.ex}
                onChange={handleAnswerChange}
                className="mr-2 w-4 h-4 text-blue-600 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-700 dark:focus:ring-offset-gray-700  dark:bg-gray-600 dark:border-gray-500"
              />
              <label
                htmlFor={`quiz-radio-${questionId}-${item.ex}`}
                className="w-full py-3 ml-2 text-[16px] text-gray-900 dark:text-gray-300">
                {item.content}
              </label>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
