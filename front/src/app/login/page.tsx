import Login from "@/components/Login";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Login",
  description: "Login to Hype Note",
};

export default function LoginPage() {
  return (
    <section className="flex justify-center mt-24">
      <Login />
    </section>
  );
}
