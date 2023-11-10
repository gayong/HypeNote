"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import Image from "next/image";
import confetti from "canvas-confetti";
import { useRouter } from "next/navigation";
import { Button, Radio } from "antd";
const quizResults = {
  quizId: 165,
  roomId: 165,
  roomName: "1111",
  totals: 2,
  examStart: "2023-11-10 11:29",
  examDone: "2023-11-10 11:31",
  questionResult: [
    {
      id: 1,
      question: "TCP/IP 통신에서 흐름 제어와 혼잡 제어는 무엇을 해결하기 위한 기법인가요?",
      example: [
        {
          ex: "1",
          content: "신뢰적인 연결 방식",
        },
        {
          ex: "2",
          content: "손실된 패킷 문제",
        },
        {
          ex: "3",
          content: "패킷의 순서가 바뀌는 문제",
        },
        {
          ex: "4",
          content: "네트워크가 혼잡한 문제",
        },
      ],
      answer: "4",
      myAnswer: "3",
      commentary:
        "흐름 제어는 송신측과 수신측의 데이터 처리 속도 차이를 해결하기 위한 기법이며, 혼잡 제어는 송신측의 데이터 전달과 네트워크의 데이터 처리 속도 차이를 해결하기 위한 기법입니다.",
    },
    {
      id: 2,
      question: "TCP의 흐름 제어와 혼잡 제어에 대한 설명으로 옳은 것은?",
      example: [
        {
          ex: "1",
          content:
            "TCP는 신뢰적인 연결 방식으로, packet의 손실 문제와 packet의 순서가 바뀌는 문제를 해결하기 위한 기법이다.",
        },
        {
          ex: "2",
          content: "흐름 제어는 송신측과 수신측의 데이터 처리 속도 차이를 해결하기 위한 기법이다.",
        },
        {
          ex: "3",
          content: "혼잡 제어는 네트워크가 혼잡한 문제와 receiver가 overload되는 문제를 해결하기 위한 기법이다.",
        },
        {
          ex: "4",
          content:
            "흐름 제어와 혼잡 제어는 모두 송신측의 데이터 전달과 네트워크의 데이터 처리 속도 차이를 해결하기 위한 기법이다.",
        },
      ],
      answer: "1",
      myAnswer: "1",
      commentary:
        "TCP는 패킷의 손실 문제와 순서가 바뀌는 문제를 해결하기 위한 신뢰적인 연결 방식이다. 흐름 제어는 송신측과 수신측의 데이터 처리 속도 차이를 해결하기 위한 기법이고, 혼잡 제어는 네트워크가 혼잡한 문제와 수신측의 오버로드 문제를 해결하기 위한 기법이다.",
    },
    {
      id: 1,
      question: "TCP/IP 통신에서 흐름 제어와 혼잡 제어는 무엇을 해결하기 위한 기법인가요?",
      example: [
        {
          ex: "1",
          content: "신뢰적인 연결 방식",
        },
        {
          ex: "2",
          content: "손실된 패킷 문제",
        },
        {
          ex: "3",
          content: "패킷의 순서가 바뀌는 문제",
        },
        {
          ex: "4",
          content: "네트워크가 혼잡한 문제",
        },
      ],
      answer: "4",
      myAnswer: "3",
      commentary:
        "흐름 제어는 송신측과 수신측의 데이터 처리 속도 차이를 해결하기 위한 기법이며, 혼잡 제어는 송신측의 데이터 전달과 네트워크의 데이터 처리 속도 차이를 해결하기 위한 기법입니다.",
    },
    {
      id: 1,
      question: "TCP/IP 통신에서 흐름 제어와 혼잡 제어는 무엇을 해결하기 위한 기법인가요?",
      example: [
        {
          ex: "1",
          content: "신뢰적인 연결 방식",
        },
        {
          ex: "2",
          content: "손실된 패킷 문제",
        },
        {
          ex: "3",
          content: "패킷의 순서가 바뀌는 문제",
        },
        {
          ex: "4",
          content: "네트워크가 혼잡한 문제",
        },
      ],
      answer: "4",
      myAnswer: "3",
      commentary:
        "흐름 제어는 송신측과 수신측의 데이터 처리 속도 차이를 해결하기 위한 기법이며, 혼잡 제어는 송신측의 데이터 전달과 네트워크의 데이터 처리 속도 차이를 해결하기 위한 기법입니다.",
    },
  ],
  correct: 0,
};

const quizRanking = [
  {
    userImg: "/assets/유령.png",
    total: 2,
    correct: 2,
    ranking: 1,
    userName: "윤자현",
    userPk: 2020,
  },
  {
    userImg: "/assets/유령.png",
    total: 2,
    correct: 0,
    ranking: 2,
    userName: "csi",
    userPk: 26,
  },
];

export default function Test2() {
  const [tab, setTab] = useState("rank");
  const router = useRouter();

  const handleGetQuiz = () => {
    console.log("틀린문제를 공개해");
  };

  const handleGetRank = () => {
    console.log("랭킹을 공개해라");
  };

  const outRoom = () => {
    router.replace("/quiz/room");
    console.log("방나가기");
  };

  useEffect(() => {
    var count = 200;
    var defaults = {
      origin: { y: 0.7 },
    };

    function fire(particleRatio: any, opts: any) {
      confetti({
        ...defaults,
        ...opts,
        particleCount: Math.floor(count * particleRatio),
      });
    }

    fire(0.25, {
      spread: 26,
      startVelocity: 55,
    });
    fire(0.2, {
      spread: 60,
    });
    fire(0.35, {
      spread: 120,
      decay: 0.91,
      scalar: 0.8,
    });
    fire(0.1, {
      spread: 160,
      startVelocity: 25,
      decay: 0.92,
      scalar: 1.2,
    });
    fire(0.1, {
      spread: 160,
      startVelocity: 45,
    });
  }, []);

  return (
    <section className="bg-white dark:bg-gray-900 w-full max-h-full">
      <span
        className="hover:text-hover_primary text-lg font-PreBd font-normal text-dark_background dark:text-font_primary absolute left-0 p-1 rounded-md outline outline-2 outline-dark_background dark:outline-font_primary "
        onClick={() => outRoom()}>
        {"<< 나가기"}
      </span>
      <div className="-mt-7 flex justify-center">
        <Radio.Group defaultValue="rank" buttonStyle="solid">
          <Radio.Button
            value="rank"
            onClick={() => {
              handleGetRank();
              setTab("rank");
            }}>
            랭킹 보기
          </Radio.Button>
          <Radio.Button
            value="quiz"
            onClick={() => {
              handleGetQuiz();
              setTab("quiz");
              console.log("랭킹으로 바꿈");
            }}>
            문제 다시보기
          </Radio.Button>
        </Radio.Group>
      </div>

      {tab === "rank" ? (
        <div className="mr-2 mt-4 h-[620px] max-h-[620px] overflow-scroll scrollbar-hide">
          <h1 className="mt-2 font-extrabold text-2xl text-primary text-center dark:text-font_primary">
            퀴즈 결과입니다!
          </h1>

          {quizRanking.map((user, index) => (
            <>
              <div key={user.userPk} className="h-[70px] flex justify-between items-center px-6 ">
                <div className="flex items-center">
                  <h1 className="font-extrabold text-3xl text-primary dark:text-font_primary mr-6 drop-shadow-lg">
                    {user.ranking}
                  </h1>
                  {/* <div className="mr-2 rounded-full w-10 h-10 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                  <Image src={user.userImg} alt="프로필 이미지" width={20} height={20} priority />
                  <div className="w-4"></div>
                  <h1>{user.userName}</h1>
                </div>
                <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                  {user.correct} / {user.total}
                </p>
              </div>
              {index !== quizRanking.length - 1 && <hr className="opacity-20" />}
            </>
          ))}
        </div>
      ) : (
        <div className="mr-2 h-[620px] max-h-[620px] flex items-center justify-center mt-5 ">
          <div className="overflow-y-scroll overflow-x-hidden mt-5 h-full">
            {quizResults?.questionResult.map((quiz, index) => (
              <div key={index} className="pt-16">
                <div className="flex items-center relative px-10">
                  <Image src="/assets/cor.png" alt="맞음" width={110} height={110} className="absolute left-0.5" />
                  {/* <Image src="/assets/wr.png" alt="틀림" width={70} height={70} className="absolute left-6 -top-10" /> */}

                  <h1 className="text-lg font-preBd text-dark_font">{quiz.id}.</h1>
                  <span className="ml-2 mt-1 text-lg font-preBd dark:text-font_primary">{quiz.question}</span>
                </div>

                {/* <h2>{quiz.myAnswer}</h2> */}

                <div className="ml-7">
                  {quiz.example.map((ex, index) => (
                    <p key={ex.ex} className="w-full">
                      <p
                        className={`pr-3 w-full py-3 mx-2 text-[16px] ${
                          // 퀴즈 정답인 것
                          quiz.answer === ex.ex ? "font-bold text-dark_font" : ""
                        }
                            ${
                              quiz.myAnswer === ex.ex && quiz.myAnswer !== quiz.answer
                                ? "underline decoration-[#ff4a4a] decoration-2 underline-offset-4 decoration-wavy font-bold"
                                : ""
                            }`}>
                        {ex.ex}. {ex.content}
                      </p>
                    </p>
                  ))}
                </div>
                <br />
              </div>
            ))}
          </div>
        </div>
      )}
    </section>
  );
}
