"use client";

import { useWebSocket } from "@/context/SocketProvider";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import { QuizRoomInfo } from "@/types/quiz";
import { Button } from "antd";
import { useRouter } from "next/navigation";
import ChatRoom from "./ChatRoom";
import QuizStart from "./QuizStart";
import QuizResult from "./QuizResult";
import Card2 from "../ui/Card2";

interface QuizRoomProps {
  roomId: number;
}

const notYet = {
  userName: "기다리는 중",
  userPk: 0,
};

export default function QuizRoom(props: QuizRoomProps) {
  const { room, quizs } = useContext(SocketContext);
  const [quizRoom, setQuizRoom] = useState<QuizRoomInfo | null>(null);
  const [ready, setReady] = useState<"unready" | "ready">("unready");
  const stompClient = useWebSocket();

  const router = useRouter();

  useEffect(() => {
    const data = {
      userPk: "2",
      userName: "isc",
    };
    if (stompClient) {
      stompClient.send(`/pub/quizroom/in/${props.roomId}`, {}, JSON.stringify(data));
    }
  }, [stompClient]);
  useEffect(() => {
    const data = {
      userPk: 2,
      action: ready === "ready" ? "ready" : "unready",
    };
    if (stompClient) {
      console.log("보냄");
      stompClient.send(`/pub/quizroom/ready/${props.roomId}`, {}, JSON.stringify(data));
    }
  }, [ready]);

  useEffect(() => {
    setQuizRoom(room);
  }, [room]);

  const outRoom = () => {
    router.push("/quiz/room");
    console.log("방나가기");
  };

  return (
    <>
      {quizs.length > 0 ? (
        // 퀴즈게임중
        <QuizStart />
      ) : (
        // 퀴즈 게임 전
        <>
          <section className="grid grid-cols-12 h-[70vh]">
            <div className="col-span-7 flex flex-col">
              <div className="flex justify-center items-center relative">
                <span
                  className="text-xl font-PreBd font-normal text-dark_background dark:text-font_primary absolute left-0"
                  onClick={() => outRoom()}>
                  {"<< 나가기  "}
                </span>
                <div className="text-center">
                  <h1 className="text-5xl my-4 font-bold dark:text-dark_font text-primary">
                    {room?.roomName} {"   "}
                    <span className="text-3xl font-normal text-dark_background dark:text-font_primary">
                      {room?.roomCnt}명 / {room?.roomMax}명
                    </span>
                  </h1>
                </div>
              </div>

              <div className="mt-1 lg:grid lg:grid-cols-3 xl:grid-cols-4 sm:gap-6 xl:gap-2 lg:space-y-0">
                {room?.users.map((user) => {
                  return (
                    <>
                      <Card2 key={user.userPk} user={user} />
                    </>
                  );
                })}
                {/* 빈 카드 추가 (정원 8명에서 빼기)*/}
                {Array((room?.inviteUsers.length ?? 0) - (room?.users.length ?? 0))
                  .fill(0)
                  .map((_, index) => (
                    <Card2 key={`notyet-${index}`} user={notYet} />
                  ))}
                {/* 빈 카드 추가 (정원 8명에서 빼기)*/}
                {Array(8 - (room?.inviteUsers.length ?? 0))
                  .fill(0)
                  .map((_, index) => (
                    <Card2 key={`empty-${index}`} />
                  ))}
              </div>
            </div>

            <div className="col-span-5 pl-10 min-h-full">
              <div className="flex-col justify-between">
                <ChatRoom roomId={props.roomId} />
                <br />
                <Button
                  className="dark:border-none dark:border-font_primary font-preRg bg-primary w-full h-16 text-3xl tracking-widest font-bold"
                  type="primary"
                  onClick={() => setReady(ready === "ready" ? "unready" : "ready")}>
                  {ready === "ready" ? "UNREADY" : "READY"}
                </Button>
              </div>
            </div>
          </section>

          <QuizResult />
        </>
      )}
    </>
  );
}
