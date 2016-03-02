package net.anotheria.portalkit.services.common.spring.springholder;

/**
 * @author bvanchuhov
 */
public class BaseEntity {

    private String name;

    public BaseEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BaseEntity setName(String name) {
        this.name = name;
        return this;
    }
}
