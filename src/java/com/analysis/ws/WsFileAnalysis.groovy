package com.analysis.ws

import java.util.regex.Pattern

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/8/20, MarkHuang,new
 * </ul>
 * @since 1/8/20
 */
class WsFileAnalysis {

    static WsFileInfo parseWsFile(File wsFile) {
        def text = parseProp(wsFile.text)
        def lines = text.split('\n|\r\n')

        def tasks = []
        def wsFileInfo = new WsFileInfo(tasks: tasks)

        def temp = []
        def nowTask = null
        def lastTaskLine = '?'
        def count = 1
        for (line in lines) {
            def pattern = Pattern.compile('^([a-z0-9A-Z_]+?)[ \t]+([\\s\\S]*)$')
            def matcher = pattern.matcher(line)
            if (matcher.find()) {
                if (nowTask != null) {
                    parseTask(nowTask, temp, wsFileInfo, tasks, lastTaskLine)
                    temp = []
                }
                nowTask = matcher.group(1)
                temp << matcher.group(2)
                lastTaskLine = count
            } else {
                if (nowTask != null && !line.matches('^[ \t]*$|^[ \t]*?#.*?$')) {
                    temp << line
                }
            }
            count++
        }
        parseTask(nowTask, temp, wsFileInfo, tasks, lastTaskLine)

        return wsFileInfo
    }

    private static void parseTask(String nowTask, List temp, WsFileInfo wsFileInfo, List tasks, Serializable lastTaskLine) {
        if (nowTask != null) {
            if (nowTask == 'NAME') {
                wsFileInfo.name = temp.join('\n')
            } else if (nowTask == 'SSH_URL') {
                wsFileInfo.sshUrl = SshUrl.valueOf(temp.join('\n'))
            } else if (nowTask == 'SCP' || nowTask == 'RUN' || nowTask == 'SSH_RUN' || nowTask == 'USER') {
                tasks << new Task(type: TaskType.valueOf(nowTask), content: temp.join('\n'))
            } else {
                throw new IllegalArgumentException("wrong title:$nowTask at line $lastTaskLine")
            }
        }
    }

    static parseScp(String scp) {
        def sp = scp.split('!')
        def result = [:]

        try {
            def targetDest = sp[0]
            def tds = targetDest.split('=>')
            result['target'] = tds[0].trim()
            result['dest'] = tds[1].trim()

            def attrs = sp[1].split(':')
            result['user'] = attrs[0]
            result['owner'] = attrs[1]
            result['permission'] = attrs[2]
        } catch (ArrayIndexOutOfBoundsException ignore) {

        }
        result
    }

    static String parseProp(String evalStr) {
        return new GroovyShell().evaluate("""
               def sysProp = System.properties
               def sysEnv = [:] << System.getenv()
               def ws = sysProp['WORK_SPACE']
               \$/${evalStr}/\$
        """)
    }


}





