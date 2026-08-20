package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * Deterministic, cumulative work counters for a thread-confined {@link UiScene}.
 *
 * <p>These counters are intentionally not timing measurements. They make
 * retained-scene regressions reproducible on headless CI and noisy developer
 * machines.</p>
 */
public record UiSceneMetrics(long layoutPasses, long commandSnapshotBuilds,
        long drawnNodeVisits, long hitTestCalls, long hitTestNodeVisits,
        long childOrderCacheBuilds, int drawCommandCount) {
}
