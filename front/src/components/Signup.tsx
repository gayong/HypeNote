'use client';
import Image from 'next/image';

import Button from './ui/Button';
import Input from './ui/Input';
import Label from './ui/Label';

import logoImg from '../../public/assets/logo_blue.png';
import krLogoImg from '../../public/assets/krlogo_blue.png';

export default function Signup() {
  const handleSignup = () => {
    console.log('회원가입하마');
  };

  return (
    <>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <Image className="mx-auto h-[20vh] w-auto" src={logoImg} alt="Hype Note 로고" priority />
          <h2 className="mt-3 text-center text-2xl leading-9 tracking-tight text-gray-700">
            회원가입 후 <Image className="inline mb-1.5 h-[1.4rem] w-auto" priority src={krLogoImg} alt="한글로고" />를
            즐겨보세요 ✏️
          </h2>
        </div>

        <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-3" action="#" method="POST">
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
              <div className="mt-2 ">
                <Input text="Password" />
              </div>
            </div>

            <div className="pt-5">
              <Button text="회원가입" onClick={handleSignup}></Button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
