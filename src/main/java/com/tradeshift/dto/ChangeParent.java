package com.tradeshift.dto;

public class ChangeParent {
    private Long toMoveId;
    private Long destinationId;

    public Long getToMoveId() {
        return toMoveId;
    }

    public void setToMoveId(Long toMoveId) {
        this.toMoveId = toMoveId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
}
