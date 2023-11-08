import { atom } from "jotai";

export const themeAtom = atom(typeof window === "undefined" ? "light" : localStorage.getItem("theme") || "light");
