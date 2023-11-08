"use client";
import { QuizUser } from "@/types/quiz";
import Image from "next/image";

// import logoimg from "../../../public/assets/logo.png"
interface Card2Props {
  user?: QuizUser;
}
export default function Card2({ user }: Card2Props) {
  return (
    <div className="h-64 w-40 flex bg-font_primary bg-opacity-50 shadow-lg items-center rounded-lg sm:flex-col">
      {user ? (
        <>
          {user.profileImg ? (
            <>
              <Image
                className="w-28 sm:rounded-none sm:rounded-l-lg py-6"
                src="/assets/유령.png"
                alt="인물사진"
                width={200}
                height={200}
              />
            </>
          ) : (
            <>
              <Image
                className="w-28 sm:rounded-none sm:rounded-l-lg py-6"
                src="/assets/유령2.png"
                alt="인물사진"
                width={200}
                height={200}
              />
            </>
          )}

          <div className="py-1">
            <h2 className="text-2xl font-bold tracking-tight text-center dark:text-white">{user.userName}</h2>
            {/* <span className="mt-3 mb-4 font-light text-gray-500 dark:text-gray-400">{user.host ? " (방장)" : ""}</span> */}
            <div className="text-primary font-PreBd text-lg dark:text-gray-400 text-center">
              {user.host ? "방장" : user.ready ? "준비 완료" : "준비 중"}
            </div>
          </div>
        </>
      ) : (
        <Image
          className="w-28 sm:rounded-none sm:rounded-l-lg py-6"
          src="/assets/까만유령.png"
          alt="인물사진"
          width={200}
          height={200}
        />
      )}
    </div>
  );
}
