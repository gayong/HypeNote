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
        "spin-slow": "spin 11s linear infinite",
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
      dark_font: "#6789f0",
      yellow: "#FEC107",
    },
    fontFamily: {
      preRg: ["preRg"],
      preLt: ["PreLt"],
      preBd: ["PreBd"],
      marker: ["Marker"],
      result: ["Result"],
      resultIta: ["ResultIta"],
      shrik: ["Shrik"],
      my: ["my"],
    },
  },
  plugins: [
    require("tailwind-scrollbar-hide"),
    function ({ addUtilities }: { addUtilities: (utils: { [key: string]: any }) => void }) {
      const newUtilities = {
        ".no-drag": {
          userSelect: "none",
          "-webkit-user-drag": "none",
          "-khtml-user-drag": "none",
          "-moz-user-drag": "none",
          "-o-user-drag": "none",
          "user-drag": "none",
        },
      };
      addUtilities(newUtilities);
    },
  ],
};

export default config;
