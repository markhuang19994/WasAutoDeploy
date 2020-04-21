package com.sql

import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.util.regex.Pattern

class DefaultSqlProcessor {

    static void main(String[] args) {
        new DefaultSqlProcessor().executeSqlScript('/home/mark/IdeaProjects/source/citi/pcl_linux/src/DeploySQL/DeployUAT20200306.sql' as File)
    }

    void executeSqlScripts(List<File> scriptFiles) {
        for (scriptFile in scriptFiles) {
            def exitCode = executeSqlScript(scriptFile)
            if (exitCode == 1)
                throw new RuntimeException('Execute sql script fail.')
        }
    }

    int executeSqlScript(File scriptFile) {
        def conn = null
        def exitCode = 0;
        try {
            println 'Process: ' + scriptFile.name
            println '=' * 60
            def sqlScriptMap = categorySql(scriptFile.text)
            def sql = MssqlProvider.newMssqlInstance()
            conn = sql.connection

            conn.setAutoCommit(false)
            processCreateTable(sqlScriptMap.createSqlList, conn)
            println ''
            processUpdateSql(sqlScriptMap.updateSqlList, conn)
            conn.commit()
            println 'Transaction success.'
        } catch (Exception e) {
            e.printStackTrace()
            if (conn != null) {
                conn.rollback()
                println 'Errors occurred, rolling back changes.'
            }
            exitCode = 1
        } finally {
            if (conn != null) {
                conn.close()
            }
        }
        return exitCode
    }

    private void processUpdateSql(List<SqlExec> updateSqlList, Connection conn) {
        println "[Update table]"
        def currentTableName = null
        for (updateSql in updateSqlList) {
            if (currentTableName == null || currentTableName != updateSql.tableName) {
                currentTableName = updateSql.tableName
            }
            println 'table:' + currentTableName
            println 'date:' + updateSql.date

            def sqlStr = updateSql.sqlStr
            println 'script:\n' + abbreviateSqlScript(sqlStr)

            def stmt = conn.createStatement()
            def ddlImpact = stmt.executeUpdate(sqlStr)
            conn.commit()
            println ddlImpact + ' rows affected.\n'
        }
    }

    private void processCreateTable(List<SqlExec> createSqlList, Connection conn) {
        if (createSqlList.size() > 0) {
            println '[Create table]'
        }

        for (createSql in createSqlList) {
            DatabaseMetaData meta = conn.getMetaData()
            ResultSet res = meta.getTables(null, null, createSql.tableName, "TABLE")

            if (res.next()) {
                println "table:${createSql.tableName} is exist, skip create script..."
            } else {
                def sqlStr = createSql.sqlStr
                println 'script:\n' + abbreviateSqlScript(sqlStr)
                def stmt = conn.createStatement()
                stmt.execute(sqlStr)
                conn.commit()
                println "create table:${createSql.tableName} success."
            }
        }
    }

    private Map categorySql(String sql) {
        def scripts = sql.split('----------------- UPDATE SCRIPT ---------------')
        List<SqlExec> createSqlList = null
        List<SqlExec> updateSqlList = null
        if (scripts.length > 0) {
            createSqlList = categoryCreateSql(scripts[0])
        }
        if (scripts.length > 1) {
            updateSqlList = categoryUpdateSql(scripts[1])
        }
        return [createSqlList: createSqlList, updateSqlList: updateSqlList]
    }

    private categoryCreateSql(String sql) {
        def pattern = '^(.*)\\.sql\r?\n([\\s\\S]*)$'
        sql.split('-- Table name: ').collect {
            if (it.matches(pattern)) {
                def m = Pattern.compile(pattern).matcher(it)
                if (m.find()) {
                    return new SqlExec(tableName: m.group(1), date: '', sqlStr: m.group(2).replaceAll('GO\r?\n', ''))
                }
            }
            return null
        }.findAll { it != null }
    }

    private List<SqlExec> categoryUpdateSql(String sql) {
        def tableNamePattern = '^--(.*)\\.sql$'
        def datePattern = '^--\\{} (\\d{4},\\d{2},\\d{2})$'
        sql.split('GO\r?\n').collect {
            it.split('\r?\n')
        }.collect {
            def tableName = ''
            def date = null
            def sqlStrList = []
            def SqlExecList = []
            it.eachWithIndex { line, idx ->
                if (idx == 0 && line.matches(tableNamePattern)) {
                    tableName = line.replaceAll(tableNamePattern, '$1')
                    return
                }
                if (line.matches(datePattern)) {
                    def newDate = line.replaceAll(datePattern, '$1').replace(',', '/')
                    if (date != null && newDate != date) {
                        SqlExecList << new SqlExec(tableName: tableName, date: date, sqlStr: sqlStrListToString(sqlStrList))
                        sqlStrList = []
                    }
                    date = newDate
                    return
                }
                if (tableName != '' && date != null) {
                    sqlStrList << line
                }
            }
            if (date != null) {
                SqlExecList << new SqlExec(tableName: tableName, date: date, sqlStr: sqlStrListToString(sqlStrList))
            }
            SqlExecList
        }.findAll { it.size() > 0 }.flatten() as List<SqlExec>
    }

    private sqlStrListToString(List<String> sqlStrList) {
        return sqlStrList.join('\r\n').replaceAll('GO\r?\n', '')
    }

    private String abbreviateSqlScript(String sqlStr, int len = 300) {
        sqlStr = sqlStr.split('\r?\n')
                .findAll { !it.matches('^\r*?--[\\s\\S]*$') }
                .findAll { !it.matches('^\r*?\n?$') }
                .join('\r\n')
        def endStr = sqlStr.length() > len ? '...' : ''
        return sqlStr.substring(0, Math.min(len, sqlStr.length())) + endStr
    }

}
