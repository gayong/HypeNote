"use client";
import { QuizUser } from "@/types/quiz";
import Image from "next/image";

// import logoimg from "../../../public/assets/logo.png"
interface Card2Props {
  user?: QuizUser;
}
export default function Card2({ user }: Card2Props) {
  return (
    <div className="h-56 min-h-56 mb-3 w-40 flex justify-center bg-font_primary bg-opacity-50 shadow-lg items-center rounded-lg sm:flex-col hover:-translate-y-2 duration-300">
      {user ? (
        <>
          {user.userImg ? (
            <>
              <Image
                className="mb-2 rounded-full object-cover h-[100px] w-[100px]"
                src={user.userImg}
                alt="인물사진"
                width={150}
                height={150}
              />
            </>
          ) : (
            <>
              <Image
                className="w-28 sm:rounded-none sm:rounded-l-lg mb-2"
                src="/assets/유령3.png"
                alt="인물사진"
                width={150}
                height={150}
              />
            </>
          )}

          <div className="py-1">
            <h2 className="text-xl font-bold tracking-tight text-center dark:text-white">{user.userName}</h2>
            {/* <span className="mt-3 mb-4 font-light text-gray-500 dark:text-gray-400">{user.host ? " (방장)" : ""}</span> */}
            <div className="text-primary font-preRg text-md text-center">
              {user.host ? "방장" : user.ready ? "준비 완료" : "준비 중"}
            </div>
          </div>
        </>
      ) : (
        <div className="min-h-56 h-56 flex items-center">
          <Image
            className="w-28 sm:rounded-none sm:rounded-l-lg py-6 opacity-30"
            src="/assets/까만유령.png"
            alt="인물사진"
            width={150}
            height={150}
          />
        </div>
      )}
    </div>
  );
}
