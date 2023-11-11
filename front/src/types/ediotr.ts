export interface NoteType {
  title: string;
  contents: string;
  url: string;
  thumbnail: string;
}

export interface NotesType {
  notes: Array<NoteType>;
}

export interface SearchType {
  url: string;
  // pagemap: PageMapType;
  title: string;
  datetime: string;
  contents: string;
  thumbnail: string;
}

export interface PageMapType {
  cse_thumbnail: Array<ThumbnailType>;
}
export interface ThumbnailType {
  src: string;
  width: number;
  height: number;
}
