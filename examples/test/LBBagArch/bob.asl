!s(alice,100).
!s(marcos,50).
!end.

+!s(_,0).
+!s(A,X)
   <- do(X);
      !s(A,X-1);
   .

+!end <- .wait(3000); .stopMAS.

