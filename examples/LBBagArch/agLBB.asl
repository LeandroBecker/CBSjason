belA.
belB.
belC.

//Critical Plans

//+cb0 : not belA <- critReac1.
//+cb0 : belA <- critReac0.

// +cb0 : not belA & not belB <- critReac3.
// +cb0 : belA & not belB <- critReac1.
// +cb0 : not belA & belB <- critReac2.
// +cb0 : belA & belB <- critReac0.

+cb0 : not belA & not belB & not belC <- critReac7.
+cb0 : not belA & not belB & belC <- critReac1.
+cb0 : not belA & belB & not belC <- critReac2.
+cb0 : not belA &  belB & belC <- critReac3.
+cb0 : belA & not belB & not belC <- critReac4.
+cb0 :  belA & not belB & belC <- critReac5.
+cb0 :  belA & belB & not belC <- critReac6.
+cb0 :  belA & belB & belC <- critReac0.


!start.
!free(5).

// very busy (100% go)
//+!start : true <- .stopMAS(10000); !go(1000000); !plan(100000).
+!start : true <- .stopMAS(1000); !!plan(100000); for ( .range(I,0,20) ) { // creates 6 concurrent intentions for g
         !!go(1000000);
      }.

//+!plan(N) : belief(N) <- !plan(N).
//-!plan(N) : true <- !plan(N).

+!plan(N) : belief(N) <- .wait(100); .print(yes); !plan(N).
-!plan(N) : true <- dummy; !plan(N).

+!go(0).
+!go(X) <- dummy; !go(X-1).

+!free(0).
@l[idle] +!free(X) <- .print(free); !free(X-1).
