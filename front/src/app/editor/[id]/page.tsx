import Editor from "@/components/editor/Editor";
import TestEditor from "@/components/editor/TestEditor";
import dynamic from "next/dynamic";

type Props = {
  params: {
    id: string;
  };
};

export default function EditorPage({ params: { id } }: Props) {
  const TestEditor = dynamic(() => import("@/components/editor/TestEditor"), { ssr: false });

  return (
    <>
      {/* <Editor id={id} /> */}
      <TestEditor id={id} />
    </>
  );
}
