package com.ygznsl.minesweeper.exception;

public abstract class MinesweeperException extends Exception
{

    protected MinesweeperException(String message)
    {
        super(message);
    }

    protected MinesweeperException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
