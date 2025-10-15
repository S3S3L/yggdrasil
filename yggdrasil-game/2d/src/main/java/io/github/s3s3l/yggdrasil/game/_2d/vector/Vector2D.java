package io.github.s3s3l.yggdrasil.game._2d.vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Vector2D {
    public float x;
    public float y;
}
