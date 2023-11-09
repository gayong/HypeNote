import TestEditorNotFirst from "@/components/editor/TestEditorNotFirst";
import dynamic from "next/dynamic";

type Props = {
  params: {
    id: string;
  };
};

export default function EditorPage({ params: { id } }: Props) {
  const TestEditor = dynamic(() => import("@/components/editor/TestEditor"), { ssr: false });
  return <> {id === "1" ? <TestEditor id={id} /> : <TestEditorNotFirst id={id} />}</>;
}
