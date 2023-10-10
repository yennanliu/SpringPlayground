package com.yen.SpringAssignmentSystem.util;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class ListUtil<U> {

     // https://stackoverflow.com/questions/189559/how-do-i-join-two-lists-in-java
     public List<U> mergeList(List<U> list1, List<U> list2){

         List<U> newList = null;

         if (list1 == null || list2 == null){
             if (list1 == null){
                 return list2;
             }
             return list1;
         }

         try{
             return Stream.concat(list1.stream(), list2.stream())
                     .collect(Collectors.toList());
         }catch (Exception e){
             log.error("Merge list error : {}", e);
             return newList;
         }
    }

}
