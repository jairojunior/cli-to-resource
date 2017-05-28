# JBoss-CLI to Puppet/Ansible

REST API to convert plain JBoss-CLI commands to Ansible/Puppet representation.

## Example

`mvn wildfly-swarm:run`

`curl -s -X POST -H "Accept: application/vnd.wildfly.resource.puppet+json" -H "Content-Type: application/json" --data '{ "commands": ["/system-property=foo:add(value=bar)"] }'  http://127.0.0.1:8080/cli | jq --raw-output .content`

## Puppet

From:

```
/subsystem=datasources/xa-data-source=petshopDSXA:add(driver-name=h2, jndi-name=\"java:jboss/datasources/petshopDSXA\", user-name=petshop, password=password, xa-datasource-class=\"org.h2.jdbcx.JdbcDataSource\")

/subsystem=datasources/xa-data-source=petshopDSXA/xa-datasource-properties=URL:add(value=\"jdbc:h2://10.10.10.10/petshop\")
```

To:

```puppet
wildfly_resource { '/subsystem=datasources/xa-data-source=petshopDSXA':
  state => {
    'driver-name'              => 'h2',
    'jndi-name'                => 'java:jboss/datasources/petshopDSXA',
    'user-name'                => 'petshop',
    'password'                 => 'password',
    'xa-datasource-class'      => 'org.h2.jdbcx.JdbcDataSource',
    'xa-datasource-properties' => {
          'url' => {'value' => 'jdbc:postgresql://10.10.10.10/petshop'}
    },
  }
}
```

## Ansible (Soon)

From:

```
/subsystem=datasources/data-source=DemoDS:add(driver-name=h2, connection-url="jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", jndi-name="java:jboss/datasources/DemoDS", user-name=sa, password=sa, min-pool-size=10, max-pool-size=30)
```


To:

```yaml
- jboss_resource:
  name: "/subsystem=datasources/data-source=DemoDS"
  state: present
  attributes:
    driver-name: h2
    connection-url: "jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    jndi-name: "java:jboss/datasources/DemoDS"
    user-name: sa
    password: sa
    min-pool-size: 10
    max-pool-size: 30
```
