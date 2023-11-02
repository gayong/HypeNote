"use client";

import Router from "next/router";
import { useState } from "react";

export const useLoading = () => {
  const [nowLoading, setNowLoading] = useState<boolean>(false);

  const start = () => setNowLoading(true);
  const end = () => setNowLoading(false);

  if (typeof window !== "undefined") {
    Router.events.on("routeChangeStart", start);
    Router.events.on("routeChangeComplete", end);
    Router.events.on("routeChangeError", end);

    return () => {
      Router.events.off("routeChangeStart", start);
      Router.events.off("routeChangeComplete", end);
      Router.events.off("routeChangeError", end);
    };
  }

  return nowLoading;
};
