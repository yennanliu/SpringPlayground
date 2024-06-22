package com.yen.mdblog.constant;

public enum PageConst {
  PAGE_SIZE(3),
  PAGE_NUM(0);

  private final int size;

  PageConst(int size) {
    this.size = size;
  }

  public int getSize() {
    return this.size;
  }
}
