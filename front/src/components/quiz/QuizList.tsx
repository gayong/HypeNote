import Link from 'next/link';

export default function QuizList() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-5 ">퀴즈 리스트를 보여주는 페이지</h1>
      <Link href="/quiz/maker">
        <button className="bg-primary flex mx-auto hover:bg-gray-300 text-secondary py-2 px-4 rounded">
          방 만들기
        </button>
      </Link>
    </div>
  );
}
