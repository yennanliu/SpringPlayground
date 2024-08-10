package com.yen.webFluxPoc.dev;

// https://youtu.be/N-c1OV3s-Tk?si=VsXAQ4gudLf6N5RQ

import java.util.ArrayList;
import java.util.List;

public class ReactiveStreamsExample {
  public static void main(String[] args) {

      List<String> myList = new ArrayList<>();
      myList.add("a");
      myList.add("b");
      myList.add("c");

      /** Example 1 : back pressure 背壓
       *
       *  1. decrease consumer pressure via buffer (e.g. publisher send msg too fast, consumer can't catch up)
       *  2. use "cache array" as buffer (緩衝區)
       *    - (instead of letting consumer read msg directly, put msg to buffer first, then consumer read from buffer)
       *  3. once consumer has capability to consume, it will send a msg to publisher, then consumer continue process data
       *
       */
//      myList.stream()
//              .filter(a -> {
//                  System.out.println(a);
//              });

  }

}
