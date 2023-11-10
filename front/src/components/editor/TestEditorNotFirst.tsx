"use client";

import { useAtom } from "jotai";
import { themeAtom } from "../../store/theme";
import { isSearchOpen } from "../../store/searchOpen";
import { useEffect, useState } from "react";
import { BlockNoteView, blockNoteToMantineTheme, useBlockNote } from "@blocknote/react";
import "@blocknote/core/style.css";
import styles from "./Editor.module.css";
import { uploadToTmpFilesDotOrg_DEV_ONLY, Block, PartialBlock } from "@blocknote/core";
import * as store from "./store";
import Search from "@/components/editor/Search";
import { useEditorWebSocket } from "@/context/SocketEditorProvider";
import { Button } from "antd";
import ShardeBtn from "./SharedBtn";
import ToShareBtn from "./ToShareBtn";

type WindowWithProseMirror = Window & typeof globalThis & { ProseMirror: any };

type Props = {
  id: string;
};

function TestEditorNotFirst({ id }: Props) {
  // const [theme, setTheme] = useState<"light" | "dark">("light");
  const [theme, setTheme] = useAtom<any>(themeAtom);
  const [open] = useAtom(isSearchOpen);
  const stompClient = useEditorWebSocket();

  useEffect(() => {
    if (stompClient) {
      store.connectStompClient(id, stompClient);
    }
    return () => {
      if (stompClient) {
        stompClient.unsubscribe(`/sub/note/${id}`);
      }
    };
  }, [id, stompClient]);

  const editor = useBlockNote({
    onEditorContentChange: (editor) => {
      console.log(editor.topLevelBlocks, "blcok value");
      console.log(editor.domElement.innerHTML);
    },
    domAttributes: {
      editor: {
        class: styles.editor,
        "data-test": "editor",
      },
    },
    uploadFile: uploadToTmpFilesDotOrg_DEV_ONLY,
    collaboration: {
      // The Yjs Provider responsible for transporting updates:
      provider: {
        connect: () => {
          // No need to connect here; it's handled in store.tsx
        },
        disconnect: () => {
          // Disconnect from SockJS and Stomp
          // Implement disconnection logic as needed
        },
      },
      // Where to store BlockNote data in the Y-Doc:
      fragment: store.store.fragment,
      // Information (name and color) for this user:
      user: {
        name: "store.getRandomName()",
        color: "#958DF1",
      },
    },
  });
  //Test
  // Give tests a way to get prosemirror instance
  (window as WindowWithProseMirror).ProseMirror = editor?._tiptapEditor;

  return (
    <>
      <div style={{ width: open ? "calc(100% - 300px)" : "100%" }}>
        <BlockNoteView editor={editor} theme={theme} />
      </div>
      <Search />
      {/* <Button className="absolute top-5 right-10">공유버튼</Button> */}
      <ShardeBtn />
      <ToShareBtn />
    </>
  );
}

export default TestEditorNotFirst;
