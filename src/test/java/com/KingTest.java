package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.King;
import main.java.com.game.Piece.Color;
import main.java.com.game.Rook;
import main.java.com.game.Vec2;

public class KingTest {
    private Board board;
    private King whiteKing;
    private Rook whiteKingsideRook;
    private Rook whiteQueensideRook;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        
        whiteKing = new King(Color.WHITE, new Vec2(4, 0));
        whiteKingsideRook = new Rook(Color.WHITE, new Vec2(7, 0));
        whiteQueensideRook = new Rook(Color.WHITE, new Vec2(0, 0));

        board.player1.addPiece(whiteKing);
        board.player1.addPiece(whiteKingsideRook);
        board.player1.addPiece(whiteQueensideRook);
    }

    @Test
    public void testMoveOneSpace() {
        Vec2 newPos = new Vec2(5, 0);
        assertTrue(whiteKing.move(newPos, board));
        assertEquals(newPos, whiteKing.getPos());
    }

    @Test
    public void testKingsideCastle() {
        assertTrue(whiteKing.move(whiteKing.kingsideCastle(board), board));
        assertEquals(new Vec2(6, 0), whiteKing.getPos());
        assertEquals(new Vec2(5, 0), whiteKingsideRook.getPos());
    }

    @Test
    public void testQueensideCastle() {
        assertTrue(whiteKing.move(whiteKing.queensideCastle(board), board));
        assertEquals(new Vec2(2, 0), whiteKing.getPos());
        assertEquals(new Vec2(3, 0), whiteQueensideRook.getPos());
    }

    @Test
    public void testCannotCastleIfChecked() {
        board.player2.addPiece(new Rook(Color.BLACK, new Vec2(5, 0)));
        assertNull(whiteKing.kingsideCastle(board));
    }

    @Test
    public void testGetMoves() {
        List<Vec2> moves = whiteKing.getMoves(board);
        assertTrue(moves.size() > 0);
        assertTrue(moves.contains(new Vec2(5, 0)));
        assertTrue(moves.contains(whiteKing.kingsideCastle(board)));
        assertTrue(moves.contains(whiteKing.queensideCastle(board)));
    }
              }
