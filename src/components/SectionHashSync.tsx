"use client";

import { useEffect } from "react";

const sectionIds = ["home", "about", "experience", "projects", "skills", "contact"] as const;

export default function SectionHashSync() {
  useEffect(() => {
    const sections = sectionIds
      .map((id) => document.getElementById(id))
      .filter((section): section is HTMLElement => Boolean(section));

    if (!sections.length) return;

    const visibleRatios = new Map<string, number>();
    let activeHash = window.location.hash.replace("#", "");

    const setHash = (id: string) => {
      if (!id || id === activeHash) return;
      activeHash = id;
      window.history.replaceState(null, "", `#${id}`);
    };

    const observer = new IntersectionObserver(
      (entries) => {
        for (const entry of entries) {
          const id = (entry.target as HTMLElement).id;
          if (entry.isIntersecting) {
            visibleRatios.set(id, entry.intersectionRatio);
          } else {
            visibleRatios.delete(id);
          }
        }

        let bestId = "";
        let bestRatio = 0;
        for (const [id, ratio] of visibleRatios.entries()) {
          if (ratio > bestRatio) {
            bestRatio = ratio;
            bestId = id;
          }
        }

        if (bestId) setHash(bestId);
      },
      {
        root: null,
        threshold: [0.15, 0.3, 0.45, 0.6, 0.75],
        rootMargin: "-18% 0px -55% 0px",
      },
    );

    sections.forEach((section) => observer.observe(section));
    return () => observer.disconnect();
  }, []);

  return null;
}
