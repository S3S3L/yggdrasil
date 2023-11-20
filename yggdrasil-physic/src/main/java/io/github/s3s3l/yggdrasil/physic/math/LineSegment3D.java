package io.github.s3s3l.yggdrasil.physic.math;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 线段
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LineSegment3D {
    private Vector3D point1;
    private Vector3D point2;
}
