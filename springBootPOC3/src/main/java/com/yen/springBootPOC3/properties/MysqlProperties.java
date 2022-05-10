package com.yen.springBootPOC3.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** book p.22
 *
 *  if we have many key-value in config
 *  we can make a "property" class,
 *  instead of hardcode in controller
 *  e.g.
 *      // inject attr val
 *     @Value("${mysql.jdbcName}")
 *     private String jdbcName;
 */

@Data
@Component
@ConfigurationProperties(prefix="mysql")
public class MysqlProperties {

    // attr
    private String jdbcName;
    private String dbUrl;
    private String userName;
    private String password;

    // getter, setter. we implement it via lombok.Data annotation
}
