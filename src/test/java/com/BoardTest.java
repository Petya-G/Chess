package test.java.com;

import main.java.com.game.Board;
import main.java.com.game.King;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Color;
import main.java.com.game.Player;
import main.java.com.game.Vec2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class BoardTest {
    private Board board;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        board.setUpBoard();
    }

    @Test
    public void testBoardInitialization() {
        assertEquals("Player1", board.player1.name);
        assertEquals("Player2", board.player2.name);
        assertEquals(Color.WHITE, board.getTurn());
    }

    @Test
    public void testSetUpBoard() {
        assertFalse(board.getPieces().isEmpty());
        assertEquals(32, board.getPieces().size()); // 16 pieces for each player
    }

    @Test
    public void testIsWithinBounds() {
        assertTrue(board.isWithinBounds(new Vec2(0, 0)));
        assertTrue(board.isWithinBounds(new Vec2(7, 7)));
        assertFalse(board.isWithinBounds(new Vec2(-1, 0)));
        assertFalse(board.isWithinBounds(new Vec2(8, 8)));
    }

    @Test
    public void testClipMovesToBoard() {
        List<Vec2> moves = List.of(new Vec2(0, 0), new Vec2(7, 7), new Vec2(-1, 0), new Vec2(8, 8));
        List<Vec2> clippedMoves = board.clipMovesToBoard(moves);
        assertEquals(2, clippedMoves.size());
        assertTrue(clippedMoves.contains(new Vec2(0, 0)));
        assertTrue(clippedMoves.contains(new Vec2(7, 7)));
    }

    @Test
    public void testGetTurn() {
        assertEquals(Color.WHITE, board.getTurn());
    }

    @Test
    public void testGetPlayer() {
        assertEquals(board.player1, board.getPlayer(Color.WHITE));
        assertEquals(board.player2, board.getPlayer(Color.BLACK));
    }

    @Test
    public void testHasPieceAt() {
        Vec2 pos = new Vec2(0, 1);
        assertTrue(board.hasPieceAt(pos, Color.BLACK));
        assertFalse(board.hasPieceAt(pos, Color.WHITE));
    }

    @Test
    public void testGetPieceAt() {
        Vec2 pos = new Vec2(0, 1);
        assertNotNull(board.getPieceAt(pos, Color.BLACK));
        assertNull(board.getPieceAt(pos, Color.WHITE));
    }

    @Test
    public void testIsDraw() {
        Board board2 = new Board("Player1", "Player2", 8);
        board2.player1.addPiece(new King(Color.WHITE, new Vec2(0, 0)));
        board2.player2.addPiece(new King(Color.BLACK, new Vec2(7, 7)));
        assertTrue(board2.isDraw());
    }

    @Test
    public void testMovePieceTo() {
        Vec2 startPos = new Vec2(1, 6);
        Vec2 endPos = new Vec2(1, 5);
        Piece piece = board.getPieceAt(startPos, Color.WHITE);
        assertNotNull(piece);
        String result = board.movePieceTo(piece, endPos);
        assertNull(result);
        assertEquals(endPos, piece.getPos());
    }

    @Test
    public void testSaveBoardTo() throws IOException {
        // Perform some legal moves
        board.movePieceTo(board.getPieceAt(new Vec2(1, 6), Color.WHITE), new Vec2(1, 5));
        board.movePieceTo(board.getPieceAt(new Vec2(6, 1), Color.BLACK), new Vec2(6, 2));
        board.movePieceTo(board.getPieceAt(new Vec2(1, 5), Color.WHITE), new Vec2(1, 4));
        board.movePieceTo(board.getPieceAt(new Vec2(6, 2), Color.BLACK), new Vec2(6, 3));

        File tempFile = File.createTempFile("chessboard", ".pgn");
        tempFile.deleteOnExit();

        board.saveBoardTo(tempFile);

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertTrue(lines.contains("[Event \"Unnamed\"]"));
        assertTrue(lines.contains("[Site \"?\"]"));
        assertTrue(lines.contains("[Date \"" + java.time.LocalDate.now() + "\"]"));
        assertTrue(lines.contains("[Round \"1\"]"));
        assertTrue(lines.contains("[White \"" + board.player1.name + "\"]"));
        assertTrue(lines.contains("[Black \"" + board.player2.name + "\"]"));

        assertTrue(lines.stream().anyMatch(line -> line.contains("1.b3 g6")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("2.b4 g5")));
    }

    @Test
    public void testLoadBoardFrom() throws IOException {
        // Create a temporary PGN file with some moves
        File tempFile = File.createTempFile("chessboard", ".pgn");
        tempFile.deleteOnExit();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("[Event \"TestEvent\"]");
            writer.newLine();
            writer.write("[Site \"Test Site\"]");
            writer.newLine();
            writer.write("[Date \"0\"]");
            writer.newLine();
            writer.write("[Round \"1\"]");
            writer.newLine();
            writer.write("[White \"Player1\"]");
            writer.newLine();
            writer.write("[Black \"Player2\"]");
            writer.newLine();
            writer.write("\n");
            writer.write("1.e4 e5 2.Nf3 Nc6 3.Bb5 a6");
            writer.newLine();
        }

        // Load the board from the file
        board.loadBoardFrom(tempFile);

        // Verify the player names
        assertEquals("Player1", board.player1.name);
        assertEquals("Player2", board.player2.name);

        // Verify the moves
        assertEquals(new Vec2(4, 3), board.getPieceAt(new Vec2(4, 3), Color.WHITE).getPos());
        assertEquals(new Vec2(4, 4), board.getPieceAt(new Vec2(4, 4), Color.BLACK).getPos());
        assertEquals(new Vec2(5, 5), board.getPieceAt(new Vec2(5, 5), Color.WHITE).getPos());
        assertEquals(new Vec2(2, 5), board.getPieceAt(new Vec2(2, 5), Color.BLACK).getPos());
        assertEquals(new Vec2(1, 7), board.getPieceAt(new Vec2(1, 7), Color.BLACK).getPos());
    }

    @Test
    public void testLoadBoardFromInvalidMove() throws IOException {
        // Create a temporary PGN file with an invalid move
        File tempFile = File.createTempFile("chessboard_invalid", ".pgn");
        tempFile.deleteOnExit();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("[Event \"Test Event\"]");
            writer.newLine();
            writer.write("[Site \"Test Site\"]");
            writer.newLine();
            writer.write("[Date \"2023.10.10\"]");
            writer.newLine();
            writer.write("[Round \"1\"]");
            writer.newLine();
            writer.write("[White \"Player1\"]");
            writer.newLine();
            writer.write("[Black \"Player2\"]");
            writer.newLine();
            writer.write("\n");
            writer.write("1. e4 e5 2. InvalidMove");
            writer.newLine();
        }

        // Load the board from the file
        board.loadBoardFrom(tempFile);

        // Verify the player names
        assertEquals("Player1", board.player1.name);
        assertEquals("Player2", board.player2.name);

        // Verify the valid moves were applied and invalid move was ignored
        assertEquals(new Vec2(4, 3), board.getPieceAt(new Vec2(4, 3), Color.WHITE).getPos());
        assertEquals(new Vec2(4, 4), board.getPieceAt(new Vec2(4, 4), Color.BLACK).getPos());
        assertNull(board.getPieceAt(new Vec2(5, 5), Color.WHITE));
    }
}
