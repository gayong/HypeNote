import QuizMaker from '@/components/quiz/QuizMaker';
import { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'QuizMaker',
  description: 'Make your own quiz',
};

export default function QuizMakerPage() {
  return (
    <section className="flex items-center justify-center h-screen">
      <QuizMaker />
    </section>
  );
}
