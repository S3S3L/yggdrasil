package io.github.s3s3l.yggdrasil.game.core.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ResourceScopeTest {

    @Test
    void closesInReverseOrderAndContinuesAfterFailure() {
        List<String> calls = new ArrayList<>();
        ResourceScope scope = new ResourceScope();
        scope.register(() -> calls.add("first"));
        scope.register(() -> {
            calls.add("broken");
            throw new IllegalStateException("close failure");
        });
        scope.register(() -> calls.add("last"));

        IllegalStateException failure = assertThrows(IllegalStateException.class, scope::close);
        scope.close();

        assertEquals(List.of("last", "broken", "first"), calls);
        assertEquals("close failure", failure.getCause().getMessage());
    }

    @Test
    void immediatelyClosesResourcesRegisteredAfterClosure() {
        List<String> calls = new ArrayList<>();
        ResourceScope scope = new ResourceScope();
        scope.close();

        scope.register(() -> calls.add("late"));

        assertEquals(List.of("late"), calls);
    }
}
