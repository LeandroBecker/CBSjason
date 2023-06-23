//bA. bB. bC. bD. bE. bF. bG. bH.

!start.

// very busy (100% go)
+!start : true <- .print("Agent started").
// for ( .range(I,0,1) ) { // creates X (3rd param) concurrent intentions
//         !!go(9);
//      }.

//+!go(0).
//+!go(X) : bA & not cA & bB & not cB & bC & not cC & bD & not cD & bE & not cE & bF & not cF & bG & not cG & bH & not cH <- !go(X-1).

/* The plans below illustrate the reading of integer values and the writing to ros topics */
+value1(V) 
   <- .print("Read value 1: ", V);
      .wait(100).
      //execute "update_topic2" upon "sample_roscore". Such action is translated to a rostopic pub
      //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","update_value2", V+1 ).

+value2(V) 
   <- .print("CRITICAL-P, value 2: ", V);
      //.wait(100);
      //execute "update_topic2" upon "sample_roscore". Such action is translated to a rostopic pub
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","update_value", V+1).

+cb0 
   <- //.print("Read value 2: ", V);
      //.wait(100);
      //execute "update_topic2" upon "sample_roscore". Such action is translated to a rostopic pub
      //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","update_time", "updateMsg").
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","update_time", "updateMsg").

/* The plans below illustrate the reading of string values and the writing to ros topics */      
+current_hour(V) : .time(H,M,S) & .concat(H,":",M,":",S,Msg)
   <-.print("Read time ", V, " - ", Msg);
     .wait(100).
     //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","update_time",Msg).
