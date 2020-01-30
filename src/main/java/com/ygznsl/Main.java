package com.ygznsl;

import static java.lang.String.valueOf;

import com.ygznsl.minesweeper.Cell;
import com.ygznsl.minesweeper.Game;
import com.ygznsl.minesweeper.Position;
import com.ygznsl.minesweeper.exception.GameOverException;
import com.ygznsl.minesweeper.exception.NoSuchCellException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;

public final class Main extends Application
{

    @Getter
    private Game game = null;

    private final GridPane grid = new GridPane();

    private boolean gameOver = false;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setScene(new Scene(createParent()));
        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        stage.show();
    }

    private void openCell(Position position)
    {
        if (gameOver)
        {
            return;
        }

        try
        {
            game.open(position.getRow(), position.getCol());
        }
        catch (NoSuchCellException ignored)
        {
        }
        catch (GameOverException ex)
        {
            gameOver = true;
        }
    }

    private void createCellButtons()
    {
        for (int row = 1; row <= game.getRows(); row++)
        {
            for (int col = 1; col <= game.getCols(); col++)
            {
                final Cell cell = game.getCell(row, col);
                final Position cellPosition = cell.getPosition();

                final Button btn = new Button();
                btn.setPrefSize(32d, 32d);

                if (cell.isMined())
                {
                    btn.setText("X");
                    btn.setStyle("-fx-text-fill: red;");
                }
                else
                {
                    final int minedNeighborCount = game.findMinedNeighborCount(cell);
                    btn.setText(valueOf(minedNeighborCount));

                    if (minedNeighborCount > 0)
                    {
                        btn.setStyle("-fx-text-fill: blue;");
                    }
                }

                cell.setOnOpened(openedCell -> {
                    btn.setDisable(true);
                    btn.setStyle("-fx-background-color: yellow;");
                });

                btn.setOnAction(actionEvent -> openCell(cellPosition));
                grid.add(btn, cellPosition.getCol() - 1, cellPosition.getRow() - 1);
            }
        }
    }

    private Parent createParent()
    {
        game = new Game(16, 30, 99);
        createCellButtons();
        return grid;
    }

}
