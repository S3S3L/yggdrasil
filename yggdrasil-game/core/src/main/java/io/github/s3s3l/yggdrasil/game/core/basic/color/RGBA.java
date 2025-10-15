package io.github.s3s3l.yggdrasil.game.core.basic.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RGBA {
    private float r;
    private float g;
    private float b;
    private float a;

    public static RGBA fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid color string: " + str);
        }
        String[] parts = str.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid color string: " + str);
        }
        try {
            float r = Float.parseFloat(parts[0].trim());
            float g = Float.parseFloat(parts[1].trim());
            float b = Float.parseFloat(parts[2].trim());
            float a = Float.parseFloat(parts[3].trim());
            return new RGBA(r, g, b, a);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid color string: " + str, e);
        }
    }
}
