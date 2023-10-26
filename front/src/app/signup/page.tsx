import { Metadata } from "next";
import Image from "next/image";

import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import Label from "../../components/ui/Label";

import logoImg from "../../../public/assets/logo_blue.png";

export const metadata: Metadata = {
  title: "Signup",
  description: "Signup to Hype Note",
};

export default function SignupPage() {
  const handleSignup = () => {
    console.log("회원가입하마");
  };

  return (
    <section className="flex pr-[19rem] align-center h-[100vh]">
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <Image
            className="mx-auto h-[20vh] w-auto"
            src={logoImg}
            alt="Hype Note 로고"
            priority
          />
          <h2 className="mt-3 text-center text-2xl leading-9 tracking-tight text-gray-700">
            회원가입 후 한입노트를 즐겨보세요 ✏️
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" action="#" method="POST">
            <div>
              <Label text="닉네임" />
              <div className="mt-2">
                <Input text="text" />
              </div>
            </div>
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
              <div className="flex items-center justify-between">
                <Label text="비밀번호 확인" />
              </div>
              <div className="mt-2">
                <Input text="Password" />
              </div>
            </div>

            <div>
              <Button text="회원가입" onClick={handleSignup}></Button>
            </div>
          </form>
        </div>
      </div>
    </section>
  );
}
