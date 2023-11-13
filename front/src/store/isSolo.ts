import { atomWithStorage } from "jotai/utils";
export const isSoloAtom = atomWithStorage<boolean | null>("isSolo", null);
