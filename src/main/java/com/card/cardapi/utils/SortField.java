package com.card.cardapi.utils;

public enum SortField {
    NAME,
    STATUS,
    COLOR,
    CREATEDAT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
