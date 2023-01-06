!plano.
//!fake(50).

+!plano : belief(N) & N<9 <- faz(N); !plano.
-!plano : true <- .wait(100);  !plano.

//+!fake(K) : belief(K) <- .wait(100); !fake(K).
//-!fake(K) : true <- .wait(100);  .print("Fake nope"); !fake(K).

