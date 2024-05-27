// Agent developed for stressing the quantity of critical beliefs (18/Jan/2024)

/* Initial beliefs */
belA.
belB.
at(P) :- pos(P,X,Y) & pos(r1,X,Y).

/* Initial goal */
!start. 

+!start : true <- !check(slots). 
//+!start : true <- .stopMAS(17000); !check(slots).

//!check(slots).

+theEnd(_) : true 
   <- .stopMAS.

+!check(slots) : not garbage(r1)
   <- next(slot);
      !check(slots).
+!check(slots).      

//@lg[atomic]
+garbage(r1) : not .desire(carry_to(r2))
   <- !carry_to(r2).

+!carry_to(R)
   <- .drop_desire(check(slots)); // stop checking
      
      // remember where to go back
      ?pos(r1,X,Y);
      -+pos(last,X,Y);

      // carry garbage to r2
      !take(garb,R);

      // goes back and continue to check
      !at(last);
      !!check(slots).

+!take(S,L) : true
   <- !ensure_pick(S);
      !at(L);
      drop(S).

+!ensure_pick(S) : garbage(r1)
   <- pick(garb);
      !ensure_pick(S).
+!ensure_pick(_).

+!at(L) : at(L).
+!at(L) <- ?pos(L,X,Y);
           move_towards(X,Y);
           !at(L).

+garbage(r2) : true <- burn(garb).


/* Critical Plans */
+cb0 [cr]: belA & belB <- critReac(0). 
+cb1 [cr]: belA & belB <- critReac(1).
+cb2 [cr]: belA & belB <- critReac(2).
+cb3 [cr]: belA & belB <- critReac(3).
+cb4 [cr]: belA & belB <- critReac(4).
+cb5 [cr]: belA & belB <- critReac(5).
+cb6 [cr]: belA & belB <- critReac(6).
+cb7 [cr]: belA & belB <- critReac(7).
+cb8 [cr]: belA & belB <- critReac(8).
+cb9 [cr]: belA & belB <- critReac(9).
+cb10 [cr]: belA & belB <- critReac(10).
+cb11 [cr]: belA & belB <- critReac(11).
+cb12 [cr]: belA & belB <- critReac(12).
+cb13 [cr]: belA & belB <- critReac(13).
+cb14 [cr]: belA & belB <- critReac(14).
+cb15 [cr]: belA & belB <- critReac(15).
+cb16 [cr]: belA & belB <- critReac(16).
+cb17 [cr]: belA & belB <- critReac(17).
+cb18 [cr]: belA & belB <- critReac(18).
+cb19 [cr]: belA & belB <- critReac(19).
+cb20 [cr]: belA & belB <- critReac(20).
+cb21 [cr]: belA & belB <- critReac(21).
+cb22 [cr]: belA & belB <- critReac(22).
+cb23 [cr]: belA & belB <- critReac(23).
+cb24 [cr]: belA & belB <- critReac(24).
+cb25 [cr]: belA & belB <- critReac(25).
+cb26 [cr]: belA & belB <- critReac(26).
+cb27 [cr]: belA & belB <- critReac(27).
+cb28 [cr]: belA & belB <- critReac(28).
+cb29 [cr]: belA & belB <- critReac(29).
+cb30 [cr]: belA & belB <- critReac(30).
+cb31 [cr]: belA & belB <- critReac(31).

/* Regular Plans needed for supporting Critical Reactions*/
+cr0Per(_) : belA & belB <- critReac(0). 
+cr1Per(_) : belA & belB <- critReac(1).
+cr2Per(_) : belA & belB <- critReac(2).
+cr3Per(_) : belA & belB <- critReac(3).
+cr4Per(_) : belA & belB <- critReac(4).
+cr5Per(_) : belA & belB <- critReac(5).
+cr6Per(_) : belA & belB <- critReac(6).
+cr7Per(_) : belA & belB <- critReac(7).
+cr8Per(_) : belA & belB <- critReac(8).
+cr9Per(_) : belA & belB <- critReac(9).
+cr10Per(_) : belA & belB <- critReac(10).
+cr11Per(_) : belA & belB <- critReac(11).
+cr12Per(_) : belA & belB <- critReac(12).
+cr13Per(_) : belA & belB <- critReac(13).
+cr14Per(_) : belA & belB <- critReac(14).
+cr15Per(_) : belA & belB <- critReac(15).
+cr16Per(_) : belA & belB <- critReac(16).
+cr17Per(_) : belA & belB <- critReac(17).
+cr18Per(_) : belA & belB <- critReac(18).
+cr19Per(_) : belA & belB <- critReac(19).
+cr20Per(_) : belA & belB <- critReac(20).
+cr21Per(_) : belA & belB <- critReac(21).
+cr22Per(_) : belA & belB <- critReac(22).
+cr23Per(_) : belA & belB <- critReac(23).
+cr24Per(_) : belA & belB <- critReac(24).
+cr25Per(_) : belA & belB <- critReac(25).
+cr26Per(_) : belA & belB <- critReac(26).
+cr27Per(_) : belA & belB <- critReac(27).
+cr28Per(_) : belA & belB <- critReac(28).
+cr29Per(_) : belA & belB <- critReac(29).
+cr30Per(_) : belA & belB <- critReac(30).
+cr31Per(_) : belA & belB <- critReac(31).

/* Dummy perception Plans */
+fakeP(K) : true <- .drop_desire(gold(K,Y)). 
