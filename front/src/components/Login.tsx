"use client";

import Image from "next/image";
import Button from "./ui/Button";
import Input from "./ui/Input";
import logoImg from "../../public/assets/logo.png";
import Label from "./ui/Label";

export default function Login() {
  const handleLogin = () => {
    console.log("로그인하마");
  };
  return (
    <>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <Image
            className="mx-auto h-20 w-auto"
            src={logoImg}
            alt="Hype Note 로고"
            priority
          />
          <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Hype Note로 로그인하세요
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" action="#" method="POST">
            <div>
              <Label text="Email" />
              <div className="mt-2">
                <Input text="email" />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <Label text="Password" />
              </div>
              <div className="mt-2">
                <Input text="Password" />
              </div>
            </div>

            <div>
              <Button text="로그인" onClick={handleLogin}></Button>
            </div>
          </form>

          <div className="relative my-3">
            <div className="relative flex items-center">
              <div className="my-4 bg-gray-600 h-[1px] w-full "></div>
            </div>
            <div className="absolute flex justify-center w-full top-1/2 transform -translate-y-1/2">
              <span className="bg-[#faf5ef] px-2">Or continue with</span>
            </div>
          </div>

          <Button text="구글 로그인" onClick={handleLogin}></Button>

          <p className="mt-10 text-center text-sm text-gray-500">
            Not a member?{" "}
            <a
              href="#"
              className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500"
            >
              회원가입
            </a>
          </p>
        </div>
      </div>
    </>
  );
}
