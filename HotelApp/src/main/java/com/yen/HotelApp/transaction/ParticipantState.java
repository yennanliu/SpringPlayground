package com.yen.HotelApp.transaction;

public enum ParticipantState {
    PREPARED,
    ABORTED,
    COMMITTED,
    ROLLED_BACK,
    TIMEOUT,
    UNKNOWN
}