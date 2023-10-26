import Signup from '@/components/Signup';
import { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Signup',
  description: 'Signup to Hype Note',
};

export default function SignupPage() {
  return (
    <section className="flex pr-[19rem] justify-center items-center h-[100vh]">
      <Signup />
    </section>
  );
}
