# Maven publishing

## Local verification and consumption

Use Java 21 and run from `yggdrasil-game`:

```bash
mvn test
mvn package
mvn install
cd integration/gradle-consumer
gradle check
```

`mvn install` publishes these local coordinates:

```text
io.github.s3s3l:yggdrasil-game:3.8.0-SNAPSHOT
io.github.s3s3l:yggdrasil-game-bom:3.8.0-SNAPSHOT
io.github.s3s3l:yggdrasil-game-core:3.8.0-SNAPSHOT
io.github.s3s3l:yggdrasil-game-2d:3.8.0-SNAPSHOT
io.github.s3s3l:yggdrasil-game-gdx:3.8.0-SNAPSHOT
io.github.s3s3l:yggdrasil-game-gdx-lwjgl3:3.8.0-SNAPSHOT
```

The root parent POM is self-contained: consumers resolve it from the same
ordinary Maven repository as the modules, not from an unpublished
`yggdrasil-parent` POM.

## Remote release

1. Set the same release version in the root POM and every child `<parent>`
   version, and set the SCM tag to the release tag before publishing.
2. Run `mvn clean verify -Prelease`. The normal build attaches source jars;
   the `release` profile also attaches javadocs.
3. Configure the target repository server credentials in `~/.m2/settings.xml`
   and deploy, for example:

   ```bash
   mvn clean deploy -Prelease \
     -DaltDeploymentRepository=release::default::https://repository.example/releases
   ```

4. Tag the matching commit, publish the root POM and every module together,
   then verify consumption from a clean Maven/Gradle cache.

For snapshot repositories use the corresponding snapshot URL. The BOM should
be imported by consumers so all Yggdrasil Game module versions remain aligned.
