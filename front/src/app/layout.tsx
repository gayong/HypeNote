import type { Metadata } from "next";
import Providers from "./providers";
// import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/components/Navbar";
// import { QueryClient, QueryClientProvider } from "react-query";
// const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: {
    default: "한입노트(Hype Note)",
    template: "한입노트(Hype Note)",
  },
  description: "한번에 모든 걸 해결 할 수 있는 나만의 한입 노트!",
  icons: {
    icon: "/favicon.ico",
  },
};

// const queryClient = new QueryClient();

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="dark:bg-dark_background dark:text-font_primary">
        {/* <QueryClientProvider client={queryClient}> */}
        <Providers>
          <header>
            <Navbar />
          </header>
          <div className="pl-[19rem]">{children}</div>
        </Providers>
        {/* </QueryClientProvider> */}
      </body>
    </html>
  );
}
