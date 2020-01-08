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
        def text = wsFile.text
        def lines = text.split('\n|\r\n')

        def tasks = []
        def wsFileInfo = new WsFileInfo(tasks: tasks)

        def temp = []
        def nowTask = null
        def lastTaskLine = '?'
        def count = 1
        for (line in lines) {
            def pattern = Pattern.compile('^([a-z0-9A-Z_]+?)[ \t]+?([\\s\\S]*)$')
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
                def sshUrl = temp.join('\n')
                def port = null
                if (sshUrl.contains(':')) {
                    def sp = sshUrl.split(':')
                    sshUrl = sp[0]
                    port = sp[1]
                }
                wsFileInfo.sshUrl = new SshUrl(url: sshUrl, port: port)
            } else if (nowTask == 'SCP' || nowTask == 'RUN') {
                tasks << new Task(type: TaskType.valueOf(nowTask), content: temp.join('\n'))
            } else {
                throw new IllegalArgumentException("wrong title:$nowTask at line $lastTaskLine")
            }
        }
    }

    static parseScp(String scp) {
        def attrs = scp.split(':')
        def result = [:]

        try {
            result['target'] = attrs[0]
            result['dest'] = attrs[1]
            result['user'] = attrs[2]
            result['owner'] = attrs[3]
            result['permission'] = attrs[4]
        } catch (ArrayIndexOutOfBoundsException ignore) {

        }
        result
    }


}




