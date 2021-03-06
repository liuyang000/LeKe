package com.rongjie.leke;

/**
 * Created by jiangliang on 2016/7/4.
 */
public class Position {
    private int leftMargin;
    private int topMargin;

    public Position(int leftMargin,int topMargin){
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (leftMargin != position.leftMargin) return false;
        return topMargin == position.topMargin;

    }

    @Override
    public int hashCode() {
        int result = leftMargin;
        result = 31 * result + topMargin;
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "leftMargin=" + leftMargin +
                ", topMargin=" + topMargin +
                '}';
    }
}
