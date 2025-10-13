package org.formacion.procesos.domains;

import java.util.Objects;
import java.util.UUID;

/**
 * @author eduglezexp
 * @version 1.0
 */

public final class JobId {
    private final String id;

    /**
     * Constructor por defecto
     */
    public JobId() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constructor con la propiedad de id
     * @param id para identificar el job
     */
    public JobId(String id) {
        this.id = Objects.requireNonNull(id);
    }

    /**
     * Getter de id
     * @return id
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof JobId)) {
            return false;
        }
        JobId jobId = (JobId) o;
        return Objects.equals(id, jobId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
