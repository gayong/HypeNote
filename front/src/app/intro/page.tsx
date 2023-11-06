import Intro from "@/components/intro/Intro";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Intro",
  description: "Intro that indroduces our site",
};

export default function IntroPage() {
  return (
    <section className="h-[400vh] bg-primary">
      <Intro />
    </section>
  );
}
