import ThreeScene from "@/components/brain/test";
import loading from "../../public/assets/loading.gif";
import { Suspense } from "react";
import Image from "next/image";

export default function Home() {
  return (
    <>
      <Suspense
        fallback={
          <div className="flex mx-auto justify-center items-center">
            <Image src={loading} alt="로딩" priority />
          </div>
        }>
        <ThreeScene />
      </Suspense>
    </>
  );
}
