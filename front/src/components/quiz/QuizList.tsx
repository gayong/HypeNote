"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import Link from "next/link";
import { useContext, useEffect, useState } from "react";
import { useWebSocket } from "@/context/SocketProvider";
import { QuizRoomInfo } from "@/types/quiz";

export default function QuizList() {
  // const { quizRooms } = useContext(SocketContext);
  const [quizRooms, setQuizRooms] = useState<Array<QuizRoomInfo>>([]);

  const stompClient = useWebSocket();

  useEffect(() => {
    if (stompClient) {
      const roomListSubscription = stompClient.subscribe("/sub/quizroom/roomList", (roomList) => {
        // console.log(roomList);
        console.log("방리스트 구독한거 나온대");
        setQuizRooms(JSON.parse(roomList.body));
      });
      stompClient.send("/pub/quizroom/roomList", {});
    }
    return stompClient?.unsubscribe("/sub/quizroom/roomList");
  }, []);

  return (
    <>
      <div className="mx-10">
        <h1 className="text-3xl font-bold mb-2 text-center">퀴즈 리스트</h1>
        <div className="flex justify-end">
          <Link href="/quiz/maker">
            <div className="flex text-font_primary justify-center rounded-md bg-primary px-3 py-1.5 text-sm font-semibold leading-6 shadow-sm hover:bg-hover_primary focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
              방 만들기
            </div>
          </Link>
        </div>
        <br />
        <div className="grid gap-6 mb-8 md:grid-cols-2">
          {quizRooms.map((room) => (
            <div key={room.id}>
              <Link href={`/quiz/room/${room.id}`}>
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
    </>
  );
}
