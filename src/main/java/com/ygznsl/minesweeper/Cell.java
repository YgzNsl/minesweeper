package com.ygznsl.minesweeper;

import com.ygznsl.minesweeper.event.CellOpenedEventHandler;
import com.ygznsl.minesweeper.exception.MinedCellException;

public interface Cell
{

    Position getPosition();

    boolean isEmpty();

    boolean isMined();

    boolean isOpened();

    void open() throws MinedCellException;

    CellOpenedEventHandler getOnOpened();

    void setOnOpened(CellOpenedEventHandler onOpened);

}
