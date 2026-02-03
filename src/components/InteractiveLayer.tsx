"use client";

import { motion, useMotionValue, useScroll, useSpring } from "framer-motion";
import { useEffect } from "react";

export default function InteractiveLayer() {
  const { scrollYProgress } = useScroll();
  const progress = useSpring(scrollYProgress, { stiffness: 120, damping: 22, mass: 0.2 });

  const mouseX = useMotionValue(0);
  const mouseY = useMotionValue(0);
  const glowX = useSpring(mouseX, { stiffness: 140, damping: 20, mass: 0.25 });
  const glowY = useSpring(mouseY, { stiffness: 140, damping: 20, mass: 0.25 });

  useEffect(() => {
    const handleMove = (event: MouseEvent) => {
      mouseX.set(event.clientX);
      mouseY.set(event.clientY);
    };

    window.addEventListener("mousemove", handleMove);
    return () => window.removeEventListener("mousemove", handleMove);
  }, [mouseX, mouseY]);

  return (
    <>
      <motion.div className="progress-bar" style={{ scaleX: progress }} />
      <motion.div className="cursor-glow" style={{ x: glowX, y: glowY, translateX: "-50%", translateY: "-50%" }} />
    </>
  );
}
