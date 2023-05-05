/* Initial goal */
//!gBusy.

+garbage(r2) : true <- burn(garb).

// mars robot 2
+!gBusy : true <- for ( .range(I,0,99) ) { // creates 6 concurrent intentions for g
         !!go(9990000);
      }.

+!go(0).
+!go(X) <- !go(X-1).

//+cb0 : true <- burn(garb).
