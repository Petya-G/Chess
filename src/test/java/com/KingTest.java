package test.java.com;

import static org.junit.Assert.assertEquals;
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
    private King blackKing;
    private Rook kingsideWhite, queensideWhite;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        whiteKing = new King(Color.WHITE, new Vec2(4, 7));
        blackKing = new King(Color.BLACK, new Vec2(4, 0));
        board.player1.addPiece(whiteKing);
        board.player2.addPiece(blackKing);

        kingsideWhite = new Rook(Color.WHITE, new Vec2(7, 7));
        queensideWhite = new Rook(Color.WHITE, new Vec2(0, 7));
    }

    @Test
    public void testKingConstructor() {
        assertEquals(Color.WHITE, whiteKing.getColor());
        assertEquals(new Vec2(4, 7), whiteKing.getPos());
        assertEquals(Color.BLACK, blackKing.getColor());
        assertEquals(new Vec2(4, 0), blackKing.getPos());
    }

    @Test
    public void testMove() {
        Vec2 newPos = new Vec2(5, 7);
        assertTrue(whiteKing.move(newPos, board));
        board.movePieceTo(whiteKing, newPos);
        assertEquals(newPos, whiteKing.getPos());
    }

    @Test
    public void testKingsideCastle() {
        board.player1.addPiece(kingsideWhite);
        Vec2 newPos = whiteKing.kingsideCastle(board);
        assertEquals(new Vec2(6, 7), newPos);
    }

    @Test
    public void testQueensideCastle() {
        board.player1.addPiece(queensideWhite);
        Vec2 newPos = whiteKing.queensideCastle(board);
        assertEquals(new Vec2(2, 7), newPos);
    }

    @Test
    public void testGetMoves() {
        List<Vec2> moves = whiteKing.getMoves(board);
        assertTrue(moves.contains(new Vec2(5, 7)));
        assertTrue(moves.contains(new Vec2(3, 7)));
    }

    @Test
    public void testGetType() {
        assertEquals(King.Type.KING, whiteKing.getType());
    }

    @Test
    public void testClone() {
        King clonedKing = (King) whiteKing.clone();
        assertEquals(whiteKing.getColor(), clonedKing.getColor());
        assertEquals(whiteKing.getPos(), clonedKing.getPos());
    }

    @Test
    public void testGetChar() {
        assertEquals('K', whiteKing.getChar());
    }
}
