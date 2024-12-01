package main.java.com.game;

import java.util.Objects;

public class Vec2 {
	public int x, y;

	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vec2(String algebraic) {
		if (algebraic.length() == 2) {
			char file = algebraic.charAt(0);
			char rank = algebraic.charAt(1);

			this.x = file - 'a';
			this.y = 8 - Character.getNumericValue(rank);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Vec2 vec = (Vec2) obj;
		return this.x == vec.x && this.y == vec.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public String toAlgebraic() {
		return (char) ('a' + x) + "" + (8 - y);
	}
}