package com.ygznsl.minesweeper.exception;

public class GameOverException extends MinesweeperException
{

    public GameOverException()
    {
        super("Game over! You lost.");
    }

}
