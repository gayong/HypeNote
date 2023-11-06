import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  darkMode: "class",
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic": "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
      animation: {
        "spin-slow": "spin 8s linear infinite",
      },
    },
    colors: {
      primary: "#2946A2",
      font_primary: "white",
      font_secondary: "black",
      hover_primary: "rgb(29 78 216)",
      line_primary: "rgb(107 114 128)",
      secondary: "#faf5ef",
      transparent: "transparent",
      dark_primary: "#4F4F4F",
      dark_background: "#424242",
    },
  },
  plugins: [],
};

export default config;
