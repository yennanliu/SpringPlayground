package com.yen.SpringAssignmentSystem.util;

import com.yen.SpringAssignmentSystem.domain.Assignment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class ListUtilTest {

    ListUtil list_util = new ListUtil();

   @Test
   public void testMergeStringList(){
       List<String> l1 = new ArrayList<>();
       l1.add("a");
       List<String> l2 = new ArrayList<>();
       l1.add("b");
       List<String> merge_l1_l2 = list_util.mergeList(l1, l2);
       System.out.println(merge_l1_l2);
   }

    @Test
    public void testMergeAssignmentList(){
        List<Assignment> a1 = new ArrayList<>();
        a1.add(new Assignment());
        List<Assignment> a2 = new ArrayList<>();
        a1.add(new Assignment());
        List<String> merge_a1_a2 = list_util.mergeList(a1, a2);
        System.out.println(merge_a1_a2);
    }

}