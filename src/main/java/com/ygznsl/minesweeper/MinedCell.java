package com.ygznsl.minesweeper;

import lombok.ToString;

@ToString(callSuper = true)
public class MinedCell extends EmptyCell
{

    public MinedCell(Game game, Position position)
    {
        super(game, position);
    }

    @Override
    public final boolean isMined()
    {
        return true;
    }

}
