"use client";
import { SocketContext } from "@/context/SocketProvider";
// import { useConnectSocket } from "@/hooks/useConnectSocket";
// import useSubscribe from "@/hooks/useSubscribe";
import { useQuery, useInfiniteQuery } from "react-query";

import Link from "next/link";
import { useContext, useEffect } from "react";

export default function QuizList() {
  const { quizRooms } = useContext(SocketContext);
  const { sendRoom, client } = useContext(SocketContext);
  useEffect(() => {
    console.log("alkdgjkladjgkajdlkg", client?.connected);
  }, []);
  return (
    <div>
      <div className="mx-10 pt-15">
        <div className="grid gap-6 mb-8 md:grid-cols-2">
          {quizRooms.map((room) => (
            <div key={room.id}>
              <Link href={`/quiz/room/${room.id}`}>
                <div className="bg-primary min-w-0 p-4 text-font_primary rounded-lg shadow-xs dark:bg-dark_primary">
                  <h4 className="mb-4 font-semibold text-gray-600 dark:text-gray-300">{room.roomName}</h4>
                  <p className="text-gray-600 dark:text-gray-400">
                    {room.createdDate}
                    <br />
                    {room.roomCnt} 명 / {room.roomMax} 명
                    <br />
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
