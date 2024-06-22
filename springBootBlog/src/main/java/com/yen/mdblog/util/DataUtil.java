package com.yen.mdblog.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataUtil {

  // https://stackoverflow.com/questions/6416706/easy-way-to-convert-iterable-to-collection
  public static <E> Collection<E> iterable2Collection(Iterable<E> iter) {
    Collection<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    return list;
  }

  public static <E> List<E> iterable2List(Iterable<E> iter) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    return list;
  }
}
