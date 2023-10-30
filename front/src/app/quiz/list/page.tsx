import QuizList from '@/components/quiz/QuizList';
import { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'QuizList',
  description: 'Show quiz lists',
};

export default function QuizListPage() {
  return (
    <section className="flex items-center justify-center h-screen">
      <QuizList />
    </section>
  );
}
