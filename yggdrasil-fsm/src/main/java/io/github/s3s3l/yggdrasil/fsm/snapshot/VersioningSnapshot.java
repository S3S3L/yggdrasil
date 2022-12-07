package io.github.s3s3l.yggdrasil.fsm.snapshot;

import io.github.s3s3l.yggdrasil.persistence.Versioning;

import lombok.Getter;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 3:28:43 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class VersioningSnapshot implements Versioning {
    @Getter
    protected String id;
    @Getter
    protected String version;

    @SuppressWarnings("unchecked")
    public <T extends VersioningSnapshot> T id(String id) {
        this.id = id;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends VersioningSnapshot> T version(String version) {
        this.version = version;
        return (T) this;
    }

}
