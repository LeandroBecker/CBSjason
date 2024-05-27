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
+cb32 [cr]: belA & belB <- critReac(32).
+cb33 [cr]: belA & belB <- critReac(33).
+cb34 [cr]: belA & belB <- critReac(34).
+cb35 [cr]: belA & belB <- critReac(35).
+cb36 [cr]: belA & belB <- critReac(36).
+cb37 [cr]: belA & belB <- critReac(37).
+cb38 [cr]: belA & belB <- critReac(38).
+cb39 [cr]: belA & belB <- critReac(39).
+cb40 [cr]: belA & belB <- critReac(40).
+cb41 [cr]: belA & belB <- critReac(41).
+cb42 [cr]: belA & belB <- critReac(42).
+cb43 [cr]: belA & belB <- critReac(43).
+cb44 [cr]: belA & belB <- critReac(44).
+cb45 [cr]: belA & belB <- critReac(45).
+cb46 [cr]: belA & belB <- critReac(46).
+cb47 [cr]: belA & belB <- critReac(47).
+cb48 [cr]: belA & belB <- critReac(48).
+cb49 [cr]: belA & belB <- critReac(49).
+cb50 [cr]: belA & belB <- critReac(50).
+cb51 [cr]: belA & belB <- critReac(51).
+cb52 [cr]: belA & belB <- critReac(52).
+cb53 [cr]: belA & belB <- critReac(53).
+cb54 [cr]: belA & belB <- critReac(54).
+cb55 [cr]: belA & belB <- critReac(55).
+cb56 [cr]: belA & belB <- critReac(56).
+cb57 [cr]: belA & belB <- critReac(57).
+cb58 [cr]: belA & belB <- critReac(58).
+cb59 [cr]: belA & belB <- critReac(59).
+cb60 [cr]: belA & belB <- critReac(60).
+cb61 [cr]: belA & belB <- critReac(61).
+cb62 [cr]: belA & belB <- critReac(62).
+cb63 [cr]: belA & belB <- critReac(63).

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
+cr32Per(_) : belA & belB <- critReac(32).
+cr33Per(_) : belA & belB <- critReac(33).
+cr34Per(_) : belA & belB <- critReac(34).
+cr35Per(_) : belA & belB <- critReac(35).
+cr36Per(_) : belA & belB <- critReac(36).
+cr37Per(_) : belA & belB <- critReac(37).
+cr38Per(_) : belA & belB <- critReac(38).
+cr39Per(_) : belA & belB <- critReac(39).
+cr40Per(_) : belA & belB <- critReac(40).
+cr41Per(_) : belA & belB <- critReac(41).
+cr42Per(_) : belA & belB <- critReac(42).
+cr43Per(_) : belA & belB <- critReac(43).
+cr44Per(_) : belA & belB <- critReac(44).
+cr45Per(_) : belA & belB <- critReac(45).
+cr46Per(_) : belA & belB <- critReac(46).
+cr47Per(_) : belA & belB <- critReac(47).
+cr48Per(_) : belA & belB <- critReac(48).
+cr49Per(_) : belA & belB <- critReac(49).
+cr50Per(_) : belA & belB <- critReac(50).
+cr51Per(_) : belA & belB <- critReac(51).
+cr52Per(_) : belA & belB <- critReac(52).
+cr53Per(_) : belA & belB <- critReac(53).
+cr54Per(_) : belA & belB <- critReac(54).
+cr55Per(_) : belA & belB <- critReac(55).
+cr56Per(_) : belA & belB <- critReac(56).
+cr57Per(_) : belA & belB <- critReac(57).
+cr58Per(_) : belA & belB <- critReac(58).
+cr59Per(_) : belA & belB <- critReac(59).
+cr60Per(_) : belA & belB <- critReac(60).
+cr61Per(_) : belA & belB <- critReac(61).
+cr62Per(_) : belA & belB <- critReac(62).
+cr63Per(_) : belA & belB <- critReac(63).

/* Dummy perception Plans */
+fakeP(K) : true <- .drop_desire(gold(K,Y)). 
