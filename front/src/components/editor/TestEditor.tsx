"use client";

import { BlockNoteView, useBlockNote } from "@blocknote/react";
import "@blocknote/core/style.css";
import styles from "./Editor.module.css";
import { uploadToTmpFilesDotOrg_DEV_ONLY } from "@blocknote/core";
import * as store from "./store";

type WindowWithProseMirror = Window & typeof globalThis & { ProseMirror: any };

type Props = {
  id: string; // id를 문자열로 지정
};

function TestEditor({ id }: Props) {
  const editor = useBlockNote({
    onEditorContentChange: (editor) => {
      console.log(editor.topLevelBlocks);
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
      provider: store.webrtcProvider(id),
      // Where to store BlockNote data in the Y.Doc:
      fragment: store.store.fragment,
      // Information (name and color) for this user:
      user: {
        name: store.getRandomName(),
        color: store.getRandomColor(),
      },
    },
  });

  // Give tests a way to get prosemirror instance
  (window as WindowWithProseMirror).ProseMirror = editor?._tiptapEditor;

  return <BlockNoteView editor={editor} />;
}

export default TestEditor;
