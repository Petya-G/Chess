package main.java.com.game;

import java.util.Objects;

public class Vec2 {
	public int x, y;

	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
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
}