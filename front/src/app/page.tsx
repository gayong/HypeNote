import Image from "next/image";
import Link from "next/link";
// import ThreeScene from "../components/ThreeScene";
import ThreeScene from "../components/ThreeScene";
// import Brain from "../components/brain/Brain";

// import dynamic from "next/dynamic";

// const DynamicBrain = dynamic(() => import("../components/brain/Brain"), { ssr: false });

export default function Home() {
  return (
    <>
      {/* <Link href="/signin">
        <h1 className="inline underline text-primary">signIn / </h1>
      </Link>
      <Link href="/signup">
        <h1 className="inline underline text-primary">signUp</h1>
      </Link> */}
      {/* <DynamicBrain /> */}
      {/* <Brain /> */}
      <ThreeScene />
    </>
  );
}
