'use client';

import Link from 'next/link';
import Image from 'next/image';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import error from '../../public/assets/404.gif';

export default function NotFoundPage() {
  const router = useRouter();

  const toMain = () => {
    setTimeout(() => {
      router.push('/');
    }, 5000);
  };

  useEffect(() => {
    toMain();
  }, []);

  return (
    <div className="flex items-center justify-center h-screen">
      <div>
        <Image className="rounded-full h-[38vh] w-auto" src={error} alt="404" priority />
        <br />
        <h1 className="text-md text-center">
          헉! 페이지를 찾을 수 없습니다 🙄
          <br />
          5초 뒤 메인페이지로 이동시켜 드릴게요
        </h1>
      </div>
    </div>
  );
}
