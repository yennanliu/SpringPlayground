package com.yen.HotelApp.transaction;

public enum TransactionState {
    ACTIVE,
    PREPARING,
    PREPARED,
    COMMITTING,
    COMMITTED,
    ROLLING_BACK,
    ROLLED_BACK,
    ABORTED,
    TIMEOUT,
    UNKNOWN
}