"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import Image from "next/image";
import Card3 from "../ui/Card3";
import first from "../../../public/assets/first.png";
import second from "../../../public/assets/second.png";
import third from "../../../public/assets/third.png";

import { Button, Radio } from "antd";

export default function QuizResult() {
  const { quizResults } = useContext(SocketContext);
  const [tab, setTab] = useState("rank");

  const rankDummy = [
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "이가영",
      correct: 8,
      total: 10,
    },
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "윤자현",
      correct: 4,
      total: 10,
    },
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "심규렬",
      correct: 4,
      total: 10,
    },
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "최상익",
      correct: 4,
      total: 10,
    },
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "이세울",
      correct: 4,
      total: 10,
    },
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "권인식",
      correct: 4,
      total: 10,
    },
    {
      profile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      name: "김동현",
      correct: 4,
      total: 10,
    },
  ];

  const handleGetQuiz = () => {
    console.log("틀린문제를 공개해");
  };

  const handleGetRank = () => {
    console.log("랭킹을 공개해라");
  };

  return (
    <section className="bg-white dark:bg-gray-900 w-full max-h-full pr-4">
      <div className="pt-6 flex justify-center">
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

          <div className="mt-3 flex justify-center items-center p-3">
            {rankDummy.length >= 3
              ? [rankDummy[1], rankDummy[0], rankDummy[2]].map((item, index) => {
                  // 멤버가 세 명일때
                  if (index === 0) {
                    // 2등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-black rounded-xl w-[150px] h-[170px]">
                        <div>
                          <Image src={second} className="w-[80px] h-auto absolute ml-[-40px] mt-[-31px]" alt="2" />
                          <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" />
                          <div className="dark:text-dark_primary flex justify-center">{item.name}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  } else if (index === 1) {
                    // 1등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-black rounded-xl w-[180px] h-[190px]">
                        <div>
                          <Image src={first} className="w-[100px] h-auto absolute ml-[-60px] mt-[-43px]" alt="1" />
                          <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" />
                          <div className="dark:text-dark_primary flex justify-center">{item.name}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  } else {
                    // 3등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center bg-font_primary shadow-lg dark:shadow-black rounded-xl w-[150px] h-[170px]">
                        <div>
                          <Image src={third} className="w-[80px] h-auto absolute ml-[-40px] mt-[-31px]" alt="3" />
                          <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" />
                          <div className="dark:text-dark_primary flex justify-center">{item.name}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  }
                })
              : rankDummy.length === 2 // 멤버가 두 명일때
              ? [rankDummy[0], rankDummy[1]].map((item, index) => {
                  if (index === 0) {
                    // 1등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-black rounded-xl w-[180px] h-[190px]">
                        <div>
                          <Image src={first} className="w-[100px] h-auto absolute ml-[-60px] mt-[-43px]" alt="1" />
                          <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" />
                          <div className="flex justify-center">{item.name}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  } else if (index === 1) {
                    // 2등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-black rounded-xl w-[150px] h-[170px]">
                        <div>
                          <Image src={second} className="w-[80px] h-auto absolute ml-[-40px] mt-[-31px]" alt="2" />
                          <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" />
                          <div className="flex justify-center">{item.name}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  }
                })
              : rankDummy.map((item, index) => {
                  // 혼자일 때 => 혹시 혼자풀기 결과도 같은 페이지 쓴다면 필요, 아님 그냥 null 해도 됨
                  return (
                    <div
                      key={index}
                      className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-black rounded-xl w-[180px] h-[190px]">
                      <div>
                        <Image src={first} className="w-[100px] h-auto absolute ml-[-60px] mt-[-43px]" alt="1" />
                        <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" />
                        <div className="flex justify-center">{item.name}</div>
                        <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                          {item.correct} / {item.total}
                        </p>
                      </div>
                    </div>
                  );
                })}
          </div>

          {rankDummy.slice(3).map((item, index) => (
            <>
              <div key={index} className="h-[70px] flex justify-between items-center px-6 ">
                <div className="flex items-center">
                  <h1 className="font-extrabold text-3xl text-primary dark:text-font_primary mr-6 drop-shadow-lg">
                    {index + 4}
                  </h1>
                  <div className="mr-2 rounded-full w-10 h-10 bg-cover bg-[url('/assets/profile.jpg')]" />
                  {/* <Image src={profile} alt={item.name} width={20} height={20} priority /> */}
                  <h1>{item.name}</h1>
                </div>
                <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                  {item.correct} / {item.total}
                </p>
              </div>
              {index !== rankDummy.length - 4 && <hr className="opacity-20" />}
            </>
          ))}
        </div>
      ) : (
        <div className="mr-2 mt-4 h-[620px] max-h-[620px] overflow-scroll flex items-center justify-center">
          {" "}
          <h1>시험지를 다시 띄워달라</h1>
        </div>
      )}
    </section>
  );
}
