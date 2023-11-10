import React from "react";
import Image from "next/image";
import first from "../../../public/assets/first.png";
import second from "../../../public/assets/second.png";
import third from "../../../public/assets/third.png";
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

export default function Rank() {
  return (
    <div>
      <div className="mt-3 flex justify-center items-center p-3">
        {rankDummy.length >= 3
          ? [rankDummy[1], rankDummy[0], rankDummy[2]].map((item, index) => {
              // 멤버가 세 명일때
              if (index === 0) {
                // 2등
                return (
                  <div
                    key={index}
                    className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[150px] h-[170px]">
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
                    className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[180px] h-[190px]">
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
                    className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[150px] h-[170px]">
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
                    className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[180px] h-[190px]">
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
                    className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[150px] h-[170px]">
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
                  className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[180px] h-[190px]">
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
    </div>
  );
}
