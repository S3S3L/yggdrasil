package io.github.s3s3l.yggdrasil.game._2d.shape.handler;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Circle2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.InfinitePlane2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Line2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Polygon2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Ray2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Rectangle2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DCircle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DInfinitePlane2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DLine2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DPolygon2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DRay2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DRectangle2DHandler;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl.Segment2DSegment2DHandler;
import io.github.s3s3l.yggdrasil.game.core.shape.handler.ShapeHandlerManager;

public class ShapeHandlerManager2D extends ShapeHandlerManager<Vector2D> {

    private static final ShapeHandlerManager2D INSTANCE = new ShapeHandlerManager2D();

    private ShapeHandlerManager2D() {
        registerBuiltIns();
    }

    public static ShapeHandlerManager2D get() {
        return INSTANCE;
    }

    private void registerBuiltIns() {
        register(new Circle2DCircle2DHandler());
        register(new Circle2DInfinitePlane2DHandler());
        register(new Circle2DLine2DHandler());
        register(new Circle2DPolygon2DHandler());
        register(new Circle2DRay2DHandler());
        register(new Circle2DRectangle2DHandler());
        register(new Circle2DSegment2DHandler());
        register(new InfinitePlane2DCircle2DHandler());
        register(new InfinitePlane2DInfinitePlane2DHandler());
        register(new InfinitePlane2DLine2DHandler());
        register(new InfinitePlane2DPolygon2DHandler());
        register(new InfinitePlane2DRay2DHandler());
        register(new InfinitePlane2DRectangle2DHandler());
        register(new InfinitePlane2DSegment2DHandler());
        register(new Line2DCircle2DHandler());
        register(new Line2DInfinitePlane2DHandler());
        register(new Line2DLine2DHandler());
        register(new Line2DPolygon2DHandler());
        register(new Line2DRay2DHandler());
        register(new Line2DRectangle2DHandler());
        register(new Line2DSegment2DHandler());
        register(new Polygon2DCircle2DHandler());
        register(new Polygon2DInfinitePlane2DHandler());
        register(new Polygon2DLine2DHandler());
        register(new Polygon2DPolygon2DHandler());
        register(new Polygon2DRay2DHandler());
        register(new Polygon2DRectangle2DHandler());
        register(new Polygon2DSegment2DHandler());
        register(new Ray2DCircle2DHandler());
        register(new Ray2DInfinitePlane2DHandler());
        register(new Ray2DLine2DHandler());
        register(new Ray2DPolygon2DHandler());
        register(new Ray2DRay2DHandler());
        register(new Ray2DRectangle2DHandler());
        register(new Ray2DSegment2DHandler());
        register(new Rectangle2DCircle2DHandler());
        register(new Rectangle2DInfinitePlane2DHandler());
        register(new Rectangle2DLine2DHandler());
        register(new Rectangle2DPolygon2DHandler());
        register(new Rectangle2DRay2DHandler());
        register(new Rectangle2DRectangle2DHandler());
        register(new Rectangle2DSegment2DHandler());
        register(new Segment2DCircle2DHandler());
        register(new Segment2DInfinitePlane2DHandler());
        register(new Segment2DLine2DHandler());
        register(new Segment2DPolygon2DHandler());
        register(new Segment2DRay2DHandler());
        register(new Segment2DRectangle2DHandler());
        register(new Segment2DSegment2DHandler());
    }
}
