"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import Link from "next/link";
import { useContext, useEffect, useState } from "react";
import { useWebSocket } from "@/context/SocketProvider";
import { QuizRoomInfo } from "@/types/quiz";
import Loading from "@/app/loading";
import { useAtom } from "jotai";
import { userAtom } from "@/store/authAtom";

export default function QuizList() {
  // const { quizRooms } = useContext(SocketContext);
  const [quizRooms, setQuizRooms] = useState<Array<QuizRoomInfo>>([]);
  const stompClient = useWebSocket();
  const [user] = useAtom(userAtom);

  useEffect(() => {
    if (stompClient) {
      const roomListSubscription = stompClient.subscribe(`/sub/quizroom/roomList/${user.userPk}`, (roomList) => {
        setQuizRooms(JSON.parse(roomList.body));
      });
      stompClient.send(`/pub/quizroom/roomList/${user.userPk}`, {});
    }
    return stompClient?.unsubscribe(`/sub/quizroom/roomList/${user.userPk}`);
  }, [stompClient]);

  return (
    <>
      <div className="pr-[50px] p-10">
        <h1 className="mt-4 text-3xl font-bold mb-2 text-center dark:text-dark_font text-primary">퀴즈 리스트</h1>

        {quizRooms.length > 0 ? (
          <>
            <h2 className="text-base font-PreBd text-center">초대된 퀴즈 방이에요. 얼른 들어가 보아요.</h2>
            <div className="flex justify-end">
              <Link href="/quiz/maker">
                <div className="flex text-font_primary justify-center rounded-md bg-primary px-3 py-1.5 text-sm leading-6 shadow-sm hover:bg-hover_primary focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                  방 만들기
                </div>
              </Link>
            </div>
            <br />

            <div className="grid gap-6 mb-8 md:grid-cols-3">
              {quizRooms.map((room) => (
                <div key={room.id}>
                  <Link href={`/quiz/room/${room.id}`}>
                    <div className="w-80 hover:outline hover:outline-offset-2 hover:outline-4 dark:hover:border-font_primary hover:outline-primary bg-font_primary bg-opacity-50 min-w-0 p-4 text-font_primary rounded-lg shadow-lg dark:bg-dark_primary">
                      <h4 className="text-xl font-bold text-dark_primary dark:text-font_primary">{room.roomName}</h4>
                      <p className="text-sm text-line_primary text-opacity-60 dark:text-opacity-40 mb-4 dark:text-font_primary">
                        {room.createdDate}
                      </p>
                      <p className="text-dark_primary dark:text-font_primary">
                        {room.roomCnt} 명 / {room.roomMax} 명
                        <br />
                        {room.content}
                      </p>
                    </div>
                  </Link>
                </div>
              ))}
            </div>
          </>
        ) : (
          <>
            <Loading />
            <div className="font-PreBd text-center">
              퀴즈 방을 불러오고 있어요
              <br />
              초대된 방이 없을 수도 있어요
              <br />
              <br />
              방을 만들어 볼까요?
            </div>

            <Link href="/quiz/maker">
              <div className="flex text-font_primary justify-center rounded-md bg-primary px-3 py-1.5 text-sm leading-6 shadow-sm hover:bg-hover_primary focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                방 만들기
              </div>
            </Link>
          </>
        )}
      </div>
    </>
  );
}
