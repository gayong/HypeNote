"use client";

import { Editor as NovelEditor } from "novel";
import styles from "./Editor.module.css";
import * as Y from "yjs";
import Collaboration from "@tiptap/extension-collaboration";
import { HocuspocusProvider } from "@hocuspocus/provider";
import { useEffect, useState } from "react";
import { JSONContent } from "@tiptap/core";

type Props = {
  id: string; // id를 문자열로 지정
};

export default function Editor({ id }: Props) {
  const [value, setValue] = useState<JSONContent>({});
  useEffect(() => {
    console.log(value);
  }, [value]);

  // 소켓 연결
  // const provider = new HocuspocusProvider({
  //   url: "ws://127.0.0.1:1234",
  //   name: "example-document",
  // });

  // document 연결
  // const ydoc = new Y.Doc();
  // console.log("dlfsdf");
  // const extensions = Collaboration.configure({
  //   document: provider.document,
  // });
  const data = {
    type: "doc",
    content: [
      {
        type: "paragraph",
        content: [{ type: "text", text: "adsfsdf" }],
      },
      {
        type: "paragraph",
        content: [{ type: "text", text: "asdfsdf" }],
      },
      {
        type: "paragraph",
        content: [{ type: "text", text: "asdfadsfasdfasd" }],
      },
      {
        type: "heading",
        content: [{ type: "text", text: "심규룔" }],
        attrs: { level: 1 },
      },
    ],
  };
  return (
    <>
      <NovelEditor
        defaultValue={data}
        onUpdate={(editor) => setValue(editor?.getJSON())}
        className={styles["editor-container"]}
        // extensions={[extensions]}
        disableLocalStorage={true}
      />
    </>
  );
}
