package com.ygznsl.minesweeper.exception;

import static java.lang.String.format;

import com.ygznsl.minesweeper.Cell;

import lombok.Getter;

public class MinedCellException extends MinesweeperException
{

    @Getter
    private final Cell cell;

    public MinedCellException(Cell cell)
    {
        super(format("This cell is mined: %s", cell.getPosition()));
        this.cell = cell;
    }

}
