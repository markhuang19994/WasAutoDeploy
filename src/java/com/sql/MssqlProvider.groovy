package com.sql

import groovy.sql.Sql

/**
 * @author MarkHuang*
 * <ul>
 *  <li>3/2/20, MarkHuang,new
 * </ul>
 * @since 3/2/20
 */
class MssqlProvider {

    def static env = System.getenv()
    def static url = env.get('mssqlUrl') ?: 'jdbc:sqlserver://newmacaque:3433;database=XCOLA'
    def static user = env.get('mssqlUser') ?: 'sa'
    def static password = env.get('mssqlPwd') ?: 'p@ssw0rd'
    def static dbConnParams = [
            url     : url,
            user    : user,
            password: password,
            driver  : 'com.microsoft.sqlserver.jdbc.SQLServerDriver'
    ]

    static Sql newMssqlInstance() {
        Sql.newInstance(dbConnParams)
    }

}
