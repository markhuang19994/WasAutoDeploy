package com.util

/**
 * @author MarkHuang*
 * <ul>
 *  <li>3/2/20, MarkHuang,new
 * </ul>
 * @since 3/2/20
 */
class PropUtil {
    static String parseProp(String evalStr) {
        return new GroovyShell().evaluate("""
               def sysProp = System.properties
               def sysEnv = [:] << System.getenv()
               def ws = sysProp['WORK_SPACE']
               \$/${evalStr}/\$
        """)
    }
}
