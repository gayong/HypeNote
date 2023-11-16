import Signin from "@/components/Signin";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Signin",
  description: "Signin to Hype Note",
};

export default function SigninPage() {
  return (
    <section className="flex items-center justify-center h-screen">
      <Signin />
    </section>
  );
}
