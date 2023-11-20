package io.github.s3s3l.yggdrasil.physic.math;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import io.github.s3s3l.yggdrasil.physic.utils.PhysicUtils3D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 有限面
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Face3D {
    /**
     * 面上各个顶点
     */
    private List<Vector3D> points;
    /**
     * 面上各个边
     */
    private List<LineSegment3D> edges;

    /**
     * 计算面的法向量
     * 
     * @return
     */
    public Vector3D getNormal() {
        Vector3D v1 = points.get(1)
                .subtract(points.get(0));
        Vector3D v2 = points.get(2)
                .subtract(points.get(0));
        return v1.crossProduct(v2);
    }

    /**
     * 计算与指定线段的交点
     * 
     * @param lineSegment
     *            线段
     * @return
     */
    public Vector3D intersectionPoint(LineSegment3D lineSegment) {
        return PhysicUtils3D.intersectionPoint(lineSegment, this);
    }
}
