"use client";

import { motion, useMotionValue, useSpring } from "framer-motion";
import { ReactNode } from "react";

type TiltCardProps = {
  children: ReactNode;
  className?: string;
};

export default function TiltCard({ children, className }: TiltCardProps) {
  const rotateX = useMotionValue(0);
  const rotateY = useMotionValue(0);
  const springX = useSpring(rotateX, { stiffness: 180, damping: 20, mass: 0.35 });
  const springY = useSpring(rotateY, { stiffness: 180, damping: 20, mass: 0.35 });

  return (
    <motion.article
      className={className}
      style={{ rotateX: springX, rotateY: springY, transformPerspective: 1000 }}
      whileHover={{ y: -8, scale: 1.01 }}
      onMouseMove={(event) => {
        const rect = event.currentTarget.getBoundingClientRect();
        const px = (event.clientX - rect.left) / rect.width - 0.5;
        const py = (event.clientY - rect.top) / rect.height - 0.5;
        rotateY.set(px * 8);
        rotateX.set(-py * 8);
      }}
      onMouseLeave={() => {
        rotateX.set(0);
        rotateY.set(0);
      }}
      transition={{ type: "spring", stiffness: 220, damping: 18, mass: 0.3 }}
    >
      {children}
    </motion.article>
  );
}
