package io.github.s3s3l.yggdrasil.game.gdx.font;

import com.badlogic.gdx.files.FileHandle;

import io.github.s3s3l.yggdrasil.game.core.font.FontRequest;

/**
 * Resolves a backend-neutral request to the FreeType file and face selected by
 * an application. Implementations can map styles to separate files or use a
 * {@link io.github.s3s3l.yggdrasil.game.core.font.FontSource#faceIndex()} font
 * collection face.
 */
@FunctionalInterface
public interface GdxFontSourceResolver {

    FileHandle resolve(FontRequest request);
}
