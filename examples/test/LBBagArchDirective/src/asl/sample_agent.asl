// Agent sample_agent in project ts_ap

/* Initial beliefs and rules */
e.
/* Initial goals */
+e <- !g; !g2.

+!g: b[ap(1000)] & c[ap(2000)] & d[ap(3000)]
    <- .print("GOAL G1").

+!g: b[ap(1000)] & c[ap(2000)]
    <- .print("GOAL G2").

+!g: b[ap(1000)]
    <- .print("GOAL G3").

+!g: h <- .print("GOAL G4!").

+!g <- .print("GOAL G5!").

+!g2: b[ap(1000)] & c[ap(2000)]
    <- .print("Plan 2!!!!!!!!!").

+!g2 <- .print("hellooooooooo").

-!g2 <- .print("FAILED!!!!").

+?b[ap(T)]
    <-  .time(HH,MM,SS,MS);
        +b[ap(T),lu(HH,MM,SS,MS)];
        .print("Active perception plan for b").

+?c[ap(T)]
    <-  .time(HH,MM,SS,MS);
        +c[ap(T),lu(HH,MM,SS,MS)];
        .print("Active perception plan for c").

+?d[ap(T)] <- .print("Active perception plan for d").

{apply_ap}
