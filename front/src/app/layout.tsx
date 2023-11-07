"use client";

import type { Metadata } from "next";
import Providers from "./providers";
import "./globals.css";
import Navbar from "@/components/Navbar";
import { usePathname } from "next/navigation";
// import { Inter } from "next/font/google";
// const inter = Inter({ subsets: ["latin"] });

// metadata 에러나면 export 붙여서 따로 모듈화하기
const metadata: Metadata = {
  title: {
    default: "한입노트(Hype Note)",
    template: "한입노트(Hype Note)",
  },
  description: "한번에 모든 걸 해결 할 수 있는 나만의 한입 노트!",
  icons: {
    icon: "/favicon.ico",
  },
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const noNavbar = pathname === "/signin" || pathname === "/signup" || pathname === "/intro";

  return (
    <html lang="en">
      <body className="dark:bg-dark_background dark:text-font_primary transition-colors duration-1000 font-preRg">
        <Providers>
          <header>
            <Navbar />
          </header>
          <div className={noNavbar ? "" : "pl-[19rem]"}>{children}</div>
        </Providers>
      </body>
    </html>
  );
}
