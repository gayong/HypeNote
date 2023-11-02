"use client";
import { SocketContext } from "@/context/SocketProvider";
// import { useConnectSocket } from "@/hooks/useConnectSocket";
// import useSubscribe from "@/hooks/useSubscribe";
import { useQuery, useInfiniteQuery } from "react-query";

import Link from "next/link";
import { useContext, useEffect } from "react";
import Button from "../../components/ui/Button";

export default function QuizList() {
  const { quizRooms, sendRoom } = useContext(SocketContext);

  const handleCreateRoom = () => {
    console.log("방만들기");
  };

  return (
    <div>
      <div className="mx-10">
        <h1 className="text-3xl font-bold mb-2 text-center">퀴즈 리스트</h1>
        <div className="flex justify-end">
          <Link href="/quiz/maker">
            <Button text="방 만들기" onClick={handleCreateRoom}></Button>
          </Link>
        </div>
        <br />
        <div className="grid gap-6 mb-8 md:grid-cols-2">
          {quizRooms.map((room) => (
            <div key={room.id}>
              <Link href={`/quiz/room/${room.id}`} onClick={() => sendRoom(room.id)}>
                <div className="hover:border-2 dark:hover:border-font_primary hover:border-primary bg-font_primary bg-opacity-50 min-w-0 p-4 text-font_primary rounded-lg shadow-lg dark:bg-dark_primary">
                  <h4 className="text-xl font-bold text-dark_primary dark:text-font_primary">{room.roomName}</h4>
                  <p className="text-sm text-line_primary text-opacity-60 dark:text-opacity-40 mb-4 dark:text-font_primary">
                    {room.createdDate}
                  </p>
                  <p className="text-dark_primary dark:text-font_primary">
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
