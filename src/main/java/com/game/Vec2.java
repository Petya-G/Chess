package main.java.com.game;

import java.util.Objects;

public class Vec2 {
	public int x, y;

	/**
	 * Konstruktor, amely a koordinátákat egész számokként inicializálja.
	 * A paraméterek a vektor x és y értékeit reprezentálják.
	 */
	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Konstruktor, amely az algebraikus jelölést konvertálja vektorrá.
	 * A bemeneti karakterlánc az algebraikus koordinátákat 'a1'-től 'h8'-ig
	 * tartalmazza.
	 */
	public Vec2(String algebraic) {
		if (algebraic.length() == 2) {
			char file = algebraic.charAt(0);
			char rank = algebraic.charAt(1);

			this.x = file - 'a';
			this.y = 8 - Character.getNumericValue(rank);
		}
	}

	/**
	 * Összehasonlítja ezt a vektort egy másik objektummal.
	 * Visszaadja, hogy az objektum azonos-e ezzel a vektorral.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Vec2 vec = (Vec2) obj;
		return this.x == vec.x && this.y == vec.y;
	}

	/**
	 * Visszaadja a vektor hash kódját.
	 * A hash kódot az x és y koordináták alapján számolja.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/**
	 * Visszaadja a vektor algebraikus jelölését.
	 * Az x és y koordináták alapján generál egy karakterláncot az 'a1'-től 'h8'-ig.
	 */
	public String toAlgebraic() {
		return (char) ('a' + x) + "" + (8 - y);
	}
}