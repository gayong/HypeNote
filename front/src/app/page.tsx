import Image from 'next/image';
import Link from 'next/link';

export default function Home() {
  return (
    <>
      <h1 className="text-3xl font-bold">여긴 뇌이자 메인페이지</h1>
      <Link href="/signin">
        <h1 className="underline text-primary">자롱이를 위한 signIn 링크</h1>
      </Link>
      <Link href="/signup">
        <h1 className="underline text-primary">자롱이를 위한 signUp 링크</h1>
      </Link>
    </>
  );
}
