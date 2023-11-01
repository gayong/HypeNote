"use client";
// import { useConnectSocket } from "@/hooks/useConnectSocket";
// import useSubscribe from "@/hooks/useSubscribe";

import Link from "next/link";

export default function QuizList() {
  // useConnectSocket();
  // useConnectSocket("/sub/quizroom/roomList");
  // useConnectSocket("/sub/quizroom/detail/", 1);

  return (
    <div>
      <div className="mx-10 pt-15">
        <div className="grid gap-6 mb-8 md:grid-cols-2">
          {Array.from({ length: 10 }).map((_, id) => (
            <div key={id}>
              <Link href={`/quiz/room/${id}`}>
                <div className="bg-primary min-w-0 p-4 text-font_primary rounded-lg shadow-xs dark:bg-dark_primary">
                  <h4 className="mb-4 font-semibold text-gray-600 dark:text-gray-300">방이름</h4>
                  <p className="text-gray-600 dark:text-gray-400">
                    방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보 방정보
                  </p>
                </div>
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
