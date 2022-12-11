package util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector2D {
    private int X;
    private int Y;

    public Vector2D() {
        X = 0; Y = 0;
    }

    public Vector2D(int pX, int pY) {
        X = pX; Y = pY;
    }
}
