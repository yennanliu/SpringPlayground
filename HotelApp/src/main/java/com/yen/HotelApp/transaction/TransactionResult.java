package com.yen.HotelApp.transaction;

public enum TransactionResult {
    COMMITTED,
    ROLLED_BACK,
    ABORTED,
    TIMEOUT,
    UNKNOWN
}