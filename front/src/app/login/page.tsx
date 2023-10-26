import Login from '@/components/Login';
import { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Login',
  description: 'Login to Hype Note',
};

export default function LoginPage() {
  return (
    <section className="flex pr-[19rem] align-center h-[100vh]">
      <Login />
    </section>
  );
}
