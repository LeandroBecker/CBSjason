belA.
belB.
belC.

//Critical Plans

+cb0 : not belA <- critReac1.
+cb0 : belA <- critReac0.

//+cb0 : not belA & not belB <- critReac3.
//+cb0 : belA & not belB <- critReac1.
//+cb0 : not belA & belB <- critReac2.
//+cb0 : belA & belB <- critReac0.

//+cb0 : not belA & not belB & not belC <- critReac7.
//+cb0 : not belA & not belB & belC <- critReac6.
//+cb0 : not belA & belB & not belC <- critReac5.
//+cb0 : not belA &  belB & belC <- critReac4.
//+cb0 : belA & not belB & not belC <- critReac3.
//+cb0 :  belA & not belB & belC <- critReac2.
//+cb0 :  belA & belB & not belC <- critReac1.
//+cb0 :  belA & belB & belC <- critReac0.

//r7 :- not belA & not belB & not belC.
//r6 :- not belA & not belB & belC.
//r5 :- not belA & belB & not belC.
//r4 :- not belA &  belB & belC.
//r3 :- belA & not belB & not belC.
//r2 :- belA & not belB & belC.
//r1 :- belA & belB & not belC.
//r0. // :-  belA.
//r1.
//r2.
//r3.
//r4.
//r5.
//r6.
//r7.
//+cb0 : r0 <- critReac0.

!start.
!free(5).

// very busy (100% go)
//+!start : true <- .stopMAS(10000); !go(1000000); !plan(100000).
//troquei o tempo de .stopMAS, de 1k para 10k
+!start : true <- .stopMAS(60000); !!plan(100000); for ( .range(I,0,20) ) { // creates 6 concurrent intentions for g
         !!go(1000000);
      }.

+!plan(N) : belief(N) <- .wait(100); .print(yes); !plan(N).
-!plan(N) : true <- dummy; !plan(N).

+!go(0).
//+!go(X) <- !go(X-1).
+!go(X) <- dummy; !go(X-1).

+!free(0).
@l[idle] +!free(X) <- .print(free); !free(X-1).
