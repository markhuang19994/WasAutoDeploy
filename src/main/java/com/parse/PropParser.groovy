package com.parse

class PropParser {
    private def varMap = [
            sysProp: 'System.properties',
            sysEnv : '[:] << System.getenv()',
            ws     : "String.valueOf(sysProp['WORK_SPACE']).replace('\\\\', '/')"
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
