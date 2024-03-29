package io.github.s3s3l.yggdrasil.rpc.grpc.test.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.Feature;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.Point;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.Rectangle;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.RouteGuideGrpc.RouteGuideImplBase;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.RouteNote;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.RouteSummary;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.utils.RouteGuideUtil;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteGuideService extends RouteGuideImplBase {

    private final Collection<Feature> features;
    private final ConcurrentMap<Point, List<RouteNote>> routeNotes = new ConcurrentHashMap<Point, List<RouteNote>>();

    public RouteGuideService(Collection<Feature> features) {
        this.features = features;
    }

    /**
     * Gets the {@link Feature} at the requested {@link Point}. If no feature at
     * that location exists, an unnamed feature is returned at the provided
     * location.
     *
     * @param request
     *            the requested location for the feature.
     * @param responseObserver
     *            the observer that will receive the feature at the requested
     *            point.
     */
    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
        responseObserver.onNext(checkFeature(request));
        responseObserver.onCompleted();
    }

    /**
     * Gets all features contained within the given bounding {@link Rectangle}.
     *
     * @param request
     *            the bounding rectangle for the requested features.
     * @param responseObserver
     *            the observer that will receive the features.
     */
    @Override
    public void listFeatures(Rectangle request, StreamObserver<Feature> responseObserver) {
        int left = Math.min(request.getLo()
                .getLongitude(),
                request.getHi()
                        .getLongitude());
        int right = Math.max(request.getLo()
                .getLongitude(),
                request.getHi()
                        .getLongitude());
        int top = Math.max(request.getLo()
                .getLatitude(),
                request.getHi()
                        .getLatitude());
        int bottom = Math.min(request.getLo()
                .getLatitude(),
                request.getHi()
                        .getLatitude());

        for (Feature feature : features) {
            if (!RouteGuideUtil.exists(feature)) {
                continue;
            }

            int lat = feature.getLocation()
                    .getLatitude();
            int lon = feature.getLocation()
                    .getLongitude();
            if (lon >= left && lon <= right && lat >= bottom && lat <= top) {
                responseObserver.onNext(feature);
            }
        }
        responseObserver.onCompleted();
    }

    /**
     * Gets a stream of points, and responds with statistics about the "trip":
     * number of points, number of known features visited, total distance
     * traveled, and total time spent.
     *
     * @param responseObserver
     *            an observer to receive the response summary.
     * @return an observer to receive the requested route points.
     */
    @Override
    public StreamObserver<Point> recordRoute(final StreamObserver<RouteSummary> responseObserver) {
        return new StreamObserver<Point>() {
            int pointCount;
            int featureCount;
            int distance;
            Point previous;
            final long startTime = System.nanoTime();

            @Override
            public void onNext(Point point) {
                pointCount++;
                if (RouteGuideUtil.exists(checkFeature(point))) {
                    featureCount++;
                }
                // For each point after the first, add the incremental distance
                // from the previous point to
                // the total distance value.
                if (previous != null) {
                    distance += calcDistance(previous, point);
                }
                previous = point;
            }

            @Override
            public void onError(Throwable t) {
                log.warn("recordRoute cancelled");
            }

            @Override
            public void onCompleted() {
                long seconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                responseObserver.onNext(RouteSummary.newBuilder()
                        .setPointCount(pointCount)
                        .setFeatureCount(featureCount)
                        .setDistance(distance)
                        .setElapsedTime((int) seconds)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * Receives a stream of message/location pairs, and responds with a stream
     * of all previous messages at each of those locations.
     *
     * @param responseObserver
     *            an observer to receive the stream of previous messages.
     * @return an observer to handle requested message/location pairs.
     */
    @Override
    public StreamObserver<RouteNote> routeChat(final StreamObserver<RouteNote> responseObserver) {
        return new StreamObserver<RouteNote>() {
            @Override
            public void onNext(RouteNote note) {
                List<RouteNote> notes = getOrCreateNotes(note.getLocation());

                // Respond with all previous notes at this location.
                for (RouteNote prevNote : notes.toArray(new RouteNote[0])) {
                    responseObserver.onNext(prevNote);
                }

                // Now add the new note to the list
                notes.add(note);
            }

            @Override
            public void onError(Throwable t) {
                log.warn("routeChat cancelled");
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * Get the notes list for the given location. If missing, create it.
     */
    private List<RouteNote> getOrCreateNotes(Point location) {
        List<RouteNote> notes = Collections.synchronizedList(new ArrayList<RouteNote>());
        List<RouteNote> prevNotes = routeNotes.putIfAbsent(location, notes);
        return prevNotes != null ? prevNotes : notes;
    }

    /**
     * Gets the feature at the given point.
     *
     * @param location
     *            the location to check.
     * @return The feature object at the point. Note that an empty name
     *         indicates no feature.
     */
    private Feature checkFeature(Point location) {
        for (Feature feature : features) {
            if (feature.getLocation()
                    .getLatitude() == location.getLatitude()
                    && feature.getLocation()
                            .getLongitude() == location.getLongitude()) {
                return feature;
            }
        }

        // No feature was found, return an unnamed feature.
        return Feature.newBuilder()
                .setName("")
                .setLocation(location)
                .build();
    }

    /**
     * Calculate the distance between two points using the "haversine" formula.
     * The formula is based on
     * http://mathforum.org/library/drmath/view/51879.html.
     *
     * @param start
     *            The starting point
     * @param end
     *            The end point
     * @return The distance between the points in meters
     */
    private static int calcDistance(Point start, Point end) {
        int r = 6371000; // earth radius in meters
        double lat1 = Math.toRadians(RouteGuideUtil.getLatitude(start));
        double lat2 = Math.toRadians(RouteGuideUtil.getLatitude(end));
        double lon1 = Math.toRadians(RouteGuideUtil.getLongitude(start));
        double lon2 = Math.toRadians(RouteGuideUtil.getLongitude(end));
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (r * c);
    }

}
