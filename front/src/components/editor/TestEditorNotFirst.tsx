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
import { fromJSON } from "postcss";
import { element } from "three/examples/jsm/nodes/Nodes.js";

type WindowWithProseMirror = Window & typeof globalThis & { ProseMirror: any };

type Props = {
  id: string;
};

function TestEditorNotFirst({ id }: Props) {
  // const [theme, setTheme] = useState<"light" | "dark">("light");
  const [theme, setTheme] = useAtom<any>(themeAtom);
  const [open] = useAtom(isSearchOpen);
  const val = `<h1 class="_inlineContent_nstdf_297">가나다라 마바사</h1><p class="_inlineContent_nstdf_297">어ㄴ</p><p class="_inlineContent_nstdf_297">ㅁ나이ㅓ리ㅏㅁㄴ어리ㅏㅇㄴ</p><p class="_inlineContent_nstdf_297">ㄴㅁ아ㅣㅓ라ㅣㅇ널</p><p class="_inlineContent_nstdf_297"></p><p class="_inlineContent_nstdf_297">&#x3C;h1 class="_inlineContent_nstdf_297">가나다라 마바사&#x3C;/h1>&#x3C;p class="_inlineContent_nstdf_297">어ㄴ&#x3C;/p>&#x3C;p class="_inlineContent_nstdf_297">ㅁ나이ㅓ리ㅏㅁㄴ어리ㅏㅇㄴ&#x3C;/p>&#x3C;p class="_inlineContent_nstdf_297">ㄴㅁ아ㅣㅓ라ㅣㅇ널&#x3C;/p>&#x3C;p class="_inlineContent_nstdf_297">&#x3C;/p>&#x3C;p class="_inlineContent_nstdf_297">&#x3C;/p></p><p class="_inlineContent_nstdf_297"></p>`;

  useEffect(() => {
    store.connectStompClient(id);
  }, [id]);

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
    </>
  );
}

export default TestEditorNotFirst;
