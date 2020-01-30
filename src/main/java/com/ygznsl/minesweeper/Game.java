package com.ygznsl.minesweeper;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.ygznsl.minesweeper.event.CellOpenedEventHandler;
import com.ygznsl.minesweeper.exception.GameOverException;
import com.ygznsl.minesweeper.exception.MinedCellException;
import com.ygznsl.minesweeper.exception.NoSuchCellException;

import lombok.Getter;
import lombok.Setter;

public final class Game
{

    @Getter
    private final int rows;

    @Getter
    private final int cols;

    @Getter
    private final int mines;

    @Getter
    @Setter
    private CellOpenedEventHandler onCellOpened = null;

    private final HashMap<Position, Cell> cells = new HashMap<>();

    public Game(int rows, int cols, int mines)
    {
        if (rows < 1)
        {
            throw new IllegalArgumentException("At least 1 row is required.");
        }

        if (cols < 1)
        {
            throw new IllegalArgumentException("At least 1 column is required.");
        }

        if (mines < 1)
        {
            throw new IllegalArgumentException("At least 1 mine is required.");
        }

        if (mines >= rows * cols)
        {
            throw new IllegalArgumentException(format("There cannot be %d mines in a %dx%d game.", mines, rows, cols));
        }

        this.rows = rows;
        this.cols = cols;
        this.mines = mines;

        initializeBoard();
    }

    private void cellOpened(Cell cell)
    {
        if (nonNull(onCellOpened))
        {
            try
            {
                onCellOpened.cellOpened(cell);
            }
            catch (Exception ignored)
            {
            }
        }
    }

    private void initializeBoard()
    {
        final ArrayList<Position> allPositions = new ArrayList<>(rows * cols);
        for (int row = 1; row <= rows; row++)
        {
            for (int col = 1; col <= cols; col++)
            {
                allPositions.add(new Position(row, col));
            }
        }

        final Random random = new Random();
        for (int i = 0; i < mines; i++)
        {
            final int randomIndex = random.nextInt(allPositions.size());
            final Position randomPosition = allPositions.remove(randomIndex);
            final Cell cell = new MinedCell(this, randomPosition);
            cell.setOnOpened(this::cellOpened);
            cells.put(randomPosition, cell);
        }

        for (Position position : allPositions)
        {
            final Cell cell = new EmptyCell(this, position);
            cell.setOnOpened(this::cellOpened);
            cells.put(position, cell);
        }
    }

    private List<Cell> findNeighborsOfCell(Cell cell)
    {
        final LinkedList<Cell> neighbors = new LinkedList<>();
        final Position currentPosition = cell.getPosition();

        for (int r = -1; r <= 1; r++)
        {
            for (int c = -1; c <= 1; c++)
            {
                if (r == 0 && c == 0)
                {
                    continue;
                }

                final int row = currentPosition.getRow() + r;
                final int col = currentPosition.getCol() + c;

                if (row >= 1 && row <= rows && col >= 1 && col <= cols)
                {
                    final Position position = new Position(row, col);
                    if (cells.containsKey(position))
                    {
                        neighbors.add(cells.get(position));
                    }
                }
            }
        }

        return neighbors;
    }

    private boolean hasNoMinedNeighbor(Cell cell)
    {
        return 0 == findMinedNeighborCount(cell);
    }

    private void openRecursively(Cell cell)
    {
        if (cell.isOpened())
        {
            return;
        }

        try
        {
            cell.open();
        }
        catch (MinedCellException ignored)
        {
        }
        finally
        {
            if (hasNoMinedNeighbor(cell))
            {
                findNeighborsOfCell(cell)
                        .stream()
                        .filter(not(Cell::isMined))
                        .forEach(this::openRecursively);
            }
        }
    }

    public int findMinedNeighborCount(Cell cell)
    {
        return cell.isMined() ? -1 : (int) findNeighborsOfCell(cell)
                .stream()
                .filter(Cell::isMined)
                .count();
    }

    public Cell getCell(int row, int col)
    {
        return cells.get(new Position(row, col));
    }

    public void open(int row, int col) throws NoSuchCellException, GameOverException
    {
        final Position position = new Position(row, col);
        if (!cells.containsKey(position))
        {
            throw new NoSuchCellException(position);
        }

        final Cell cell = cells.get(position);
        if (cell.isOpened())
        {
            return;
        }

        if (cell.isMined())
        {
            throw new GameOverException();
        }

        openRecursively(cell);
    }

}
