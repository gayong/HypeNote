"use client";
import { FcGoogle } from "react-icons/fc";

import Image from "next/image";
import Button from "./ui/Button";
import Input from "./ui/Input";
import logoImg from "../../public/assets/logo_blue.png";
import Label from "./ui/Label";

export default function Signin() {
  const handleSignin = () => {
    console.log("로그인하마");
  };
  return (
    <>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <Image
            className="mx-auto h-[20vh] w-auto"
            src={logoImg}
            alt="Hype Note 로고"
            priority
          />
          <h2 className="mt-3 text-center text-2xl leading-9 tracking-tight text-gray-700">
            로그인 후 Hype Note를 즐겨보세요 ✏️
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" action="#" method="POST">
            <div>
              <Label text="이메일" />
              <div className="mt-2">
                <Input text="email" />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <Label text="비밀번호" />
              </div>
              <div className="mt-2">
                <Input text="Password" />
              </div>
            </div>

            <div>
              <Button text="로그인" onClick={handleSignin}></Button>
            </div>
          </form>

          <div className="relative my-3">
            <div className="relative flex items-center">
              <div className="my-4 bg-line_primary h-[1px] w-full "></div>
            </div>
            <div className="absolute flex justify-center w-full top-1/2 transform -translate-y-1/2">
              <span className="bg-secondary px-2">또는</span>
            </div>
          </div>

          <Button
            text="구글 로그인"
            iconImg={<FcGoogle />}
            onClick={handleSignin}
          />

          <p className="mt-10 text-center text-sm text-gray-500">
            아직 회원이 아니신가요?{" "}
            <a
              href="#"
              className="font-semibold leading-6 text-hover_primary hover:text-font_secondary"
            >
              회원가입
            </a>
          </p>
        </div>
      </div>
    </>
  );
}
