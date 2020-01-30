package com.ygznsl.minesweeper;

import static java.lang.Integer.compare;
import static java.lang.String.format;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public final class Position implements Serializable, Comparable<Position>
{

    private final int row;
    private final int col;

    public Position(int row, int col)
    {
        if (row < 1)
        {
            throw new IllegalArgumentException("Row number cannot be less than 1.");
        }

        if (col < 1)
        {
            throw new IllegalArgumentException("Column number cannot be less than 1.");
        }

        this.row = row;
        this.col = col;
    }

    @Override
    public String toString()
    {
        return format("(%d, %d)", row, col);
    }

    @Override
    public int compareTo(Position o)
    {
        return row != o.row
                ? compare(row, o.row)
                : compare(col, o.col);
    }

}
