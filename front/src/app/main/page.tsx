import ThreeScene from "@/components/brain/test";
import loading from "../../public/assets/loading.gif";
import { Suspense } from "react";
import Image from "next/image";
// import { cookies } from "next/headers";

export default function Main() {
  // const cookiesList = cookies();
  // const refreshToken = cookiesList.get("refreshToken");
  // console.log("GDGDG");
  // console.log("@@@", refreshToken);

  return (
    <div className="flex h-screen max-h-screen scrollbar-hide">
      <Suspense
        fallback={
          <div className="flex mx-auto justify-center items-center">
            <Image src={loading} alt="로딩" priority />
          </div>
        }>
        {/* <ThreeScene /> */}
      </Suspense>
    </div>
  );
}
