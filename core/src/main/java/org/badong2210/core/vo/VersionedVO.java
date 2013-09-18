package org.badong2210.core.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class VersionedVO implements Serializable {

    private Long id;
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
