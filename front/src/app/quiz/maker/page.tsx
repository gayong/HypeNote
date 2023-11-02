// "use client";

import QuizMaker from "@/components/quiz/QuizMaker";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "QuizMaker",
  description: "Make your own quiz",
};

export default function QuizMakerPage() {
  return (
    <section>
      <QuizMaker />
    </section>
  );
}
