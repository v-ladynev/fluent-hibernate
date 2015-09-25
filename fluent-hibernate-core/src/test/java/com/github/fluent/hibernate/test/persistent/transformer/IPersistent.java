package com.github.fluent.hibernate.test.persistent.transformer;

import java.io.Serializable;

/**
 *
 * @author V.Ladynev
 *
 * @param <ID>
 *            id type
 */
public interface IPersistent<ID> extends Serializable {

    ID getPid();

}
