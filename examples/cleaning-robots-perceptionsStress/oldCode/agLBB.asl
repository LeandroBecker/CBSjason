!start.
!free(5).

// very busy (100% go)
//+!start : true <- .stopMAS(10000); !go(1000000); !plan(100000).
+!start : true <- .stopMAS(100); !go(100); !plan(100000).

//+!plan(N) : belief(N) <- !plan(N).
//-!plan(N) : true <- !plan(N).

+!plan(N) : belief(N) <- .wait(100); .print(yes); !plan(N).
-!plan(N) : true <- .print(N); !plan(N).

+!go(0).
+!go(X) <- .print(X); !go(X-1).

+!free(0).
@l[idle] +!free(X) <- .print(free); !free(X-1).

//+!fake(K) : belief(K) <- .wait(100); !fake(K).
//-!fake(K) : true <- .wait(100);  .print("Fake nope"); !fake(K).

