package com.ygznsl.minesweeper.exception;

import static java.lang.String.format;

import com.ygznsl.minesweeper.Position;

import lombok.Getter;

public class NoSuchCellException extends MinesweeperException
{

    @Getter
    private final Position position;

    public NoSuchCellException(Position position)
    {
        super(format("No such cell at position: %s", position));
        this.position = position;
    }
}
