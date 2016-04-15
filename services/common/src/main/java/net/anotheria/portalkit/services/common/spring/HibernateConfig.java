package net.anotheria.portalkit.services.common.spring;

import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCConfig;
import org.configureme.annotations.ConfigureMe;

/**
 * @author bvanchuhov
 */
@ConfigureMe(allfields = true)
public class HibernateConfig extends JDBCConfig {

    private boolean showSql = false;

    public boolean isShowSql() {
        return showSql;
    }

    public HibernateConfig setShowSql(boolean showSql) {
        this.showSql = showSql;
        return this;
    }
}
