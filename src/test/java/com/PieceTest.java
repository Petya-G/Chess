package test.java.com;

import main.java.com.game.Board;
import main.java.com.game.King;
import main.java.com.game.Pawn;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Color;
import main.java.com.game.Rook;
import main.java.com.game.Vec2;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PieceTest {
    private Board board;
    private Piece whiteKing;
    private Piece blackKing;
    private Piece blackPawn;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        whiteKing = new King(Color.WHITE, new Vec2("a2"));
        blackKing = new King(Color.BLACK, new Vec2("g8"));

        board.player1.addPiece(whiteKing);
        board.player2.addPiece(blackKing);

        blackPawn = new Pawn(Color.BLACK, new Vec2("b2"));
        board.player2.addPiece(blackPawn);
    }

    @Test
    public void testFormatMove() {
        Vec2 newPos = (new Vec2("b2"));
        board.player2.removePiece(blackPawn);
        board.player1.addPiece(new Pawn(Color.WHITE, new Vec2("b5")));;
        board.player1.addPiece(new Pawn(Color.WHITE, new Vec2("c3")));;
        board.movePieceTo(whiteKing, newPos);
        String formattedMove = board.moves.get(0);
        assertEquals("1.Kb2", formattedMove);
    }

    @Test
    public void testFormatMoveWithCapture() {
        Vec2 newPos = blackPawn.getPos();
        board.movePieceTo(whiteKing, newPos);
        String formattedMove = board.moves.get(0);
        assertEquals("1.Kxb2", formattedMove);
    }

    @Test
    public void testFormatMoveWithCheck() {
        board.player2.removePiece(blackPawn);
        Rook rook = new Rook(Color.WHITE, new Vec2("a7"));
        board.player1.addPiece(rook);

        board.movePieceTo(rook, new Vec2("a8"));
        String formattedMove = board.moves.get(0);
        assertEquals("1.Ra8+", formattedMove);
    }

    @Test
    public void testFormatMoveWithCheckMate() {
        board.player2.removePiece(blackPawn);
        Rook rook = new Rook(Color.WHITE, new Vec2("a7"));
        board.player1.addPiece(rook);
        board.player1.removePiece(whiteKing);
        board.player1.addPiece(new King(Color.WHITE, new Vec2("g6")));

        board.movePieceTo(rook, new Vec2("a8"));
        String formattedMove = board.moves.get(0);
        assertEquals("1.Ra8#", formattedMove);
    }
}