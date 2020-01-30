package com.ygznsl.minesweeper;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import com.ygznsl.minesweeper.event.CellOpenedEventHandler;
import com.ygznsl.minesweeper.exception.MinedCellException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class EmptyCell implements Cell
{

    private final Game game;
    private final Position position;

    private boolean opened = false;

    @Getter
    @Setter
    private CellOpenedEventHandler onOpened = null;

    public EmptyCell(Game game, Position position)
    {
        this.game = requireNonNull(game, "Game cannot be null.");
        this.position = requireNonNull(position, "Position cannot be null.");
    }

    @Override
    public final boolean isEmpty()
    {
        return !isMined();
    }

    @Override
    public boolean isMined()
    {
        return false;
    }

    @Override
    public final void open() throws MinedCellException
    {
        if (opened)
        {
            return;
        }

        if (isMined())
        {
            throw new MinedCellException(this);
        }

        opened = true;

        if (nonNull(onOpened))
        {
            try
            {
                onOpened.cellOpened(this);
            }
            catch (Exception ignored)
            {
            }
        }
    }

}
