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
    <div>
      <ul className="w-48 text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-lg dark:bg-gray-700 dark:border-gray-600 dark:text-white">
        {example.map((item) => (
          <li key={item.ex} className="w-full border-b border-gray-200 rounded-t-lg dark:border-gray-600">
            <div className="flex items-center pl-3">
              <input
                id={`quiz-radio-${questionId}-${item.ex}`}
                type="radio"
                name={`quiz-${questionId}`}
                value={item.ex}
                checked={selectedAnswer === item.ex}
                onChange={handleAnswerChange}
                className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-700 dark:focus:ring-offset-gray-700 focus:ring-2 dark:bg-gray-600 dark:border-gray-500"
              />
              <label
                htmlFor={`quiz-radio-${questionId}-${item.ex}`}
                className="w-full py-3 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">
                {item.content}
              </label>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
