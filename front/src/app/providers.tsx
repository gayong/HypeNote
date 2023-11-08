"use client";
import { Provider } from "jotai";

import { ThemeProvider } from "next-themes";
import React from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import { ReactQueryDevtools } from "react-query/devtools";
// import { ReactQueryStreamedHydration } from "@tanstack/react-query-next-experimental";

const Providers = ({ children }: { children: React.ReactNode }) => {
  const [client] = React.useState(new QueryClient());
  return (
    <Provider>
      <ThemeProvider attribute="class">
        <QueryClientProvider client={client}>
          {children}
          <ReactQueryDevtools initialIsOpen={false} />
        </QueryClientProvider>
      </ThemeProvider>
    </Provider>
  );
};

export default Providers;
