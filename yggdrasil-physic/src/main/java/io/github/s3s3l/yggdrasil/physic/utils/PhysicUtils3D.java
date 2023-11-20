package io.github.s3s3l.yggdrasil.physic.utils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import io.github.s3s3l.yggdrasil.physic.math.Face3D;
import io.github.s3s3l.yggdrasil.physic.math.LineSegment3D;

public abstract class PhysicUtils3D {

    private PhysicUtils3D() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 计算线段与有限面的交点
     * 
     * @param lineSegment
     *            线段
     * @param face
     *            面
     * @return 交点；如果没有交点则返回null
     */
    public static Vector3D intersectionPoint(LineSegment3D lineSegment, Face3D face) {
        Vector3D edgeDirection = lineSegment.getPoint2()
                .subtract(lineSegment.getPoint1());
        Vector3D faceNormal = face.getNormal();
        double t = faceNormal.dotProduct(face.getPoints()
                .get(0)
                .subtract(lineSegment.getPoint1())) / faceNormal.dotProduct(edgeDirection);

        if (t >= 0 && t <= 1) {
            return lineSegment.getPoint1()
                    .add(edgeDirection.scalarMultiply(t));
        } else {
            return null; // No intersection
        }
    }
}
