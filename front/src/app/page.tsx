import ThreeScene from "@/components/brain/test";
import { Suspense } from "react";

export default function Home() {
  return (
    <>
      <Suspense fallback={<p style={{ textAlign: "center" }}>loading... on initial request</p>}>
        <ThreeScene />
      </Suspense>
    </>
  );
}
