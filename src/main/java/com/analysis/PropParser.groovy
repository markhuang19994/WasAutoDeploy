package com.analysis

class PropParser {
    private def varMap = [
            sysProp: 'System.properties',
            sysEnv : '[:] << System.getenv()',
            ws     : 'sysProp[\'WORK_SPACE\']'
    ]

    void updateVarMap(String varName, String val) {
        varMap[varName] = "\"${val}\"".toString()
    }

    public Map<String, String> getVarMap() {
        new HashMap<String, String>(varMap)
    }

    private String getDeclareStr() {
        varMap.collect {
            "def ${it.key} = ${it.value}"
        }.join('\n')
    }

    String parseString(String str) {
        return new GroovyShell().evaluate("""
                   ${getDeclareStr()}
                   \$/${str}/\$
            """)
    }
}
