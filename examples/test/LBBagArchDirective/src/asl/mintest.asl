// Agent sample_agent in project ts_ap

/* Initial beliefs and rules */
e.
/* Initial goals */
+e <- !g2.

+cb0[cp] <- .print("CT plan").

+!g2: b[ap(1000)] & c[ap(2000)]
    <- .print("Plan 2!!!!!!!!!").

+!g2 <- .print("hellooooooooo").

-!g2 <- .print("FAILED!!!!").

{apply_ct}
