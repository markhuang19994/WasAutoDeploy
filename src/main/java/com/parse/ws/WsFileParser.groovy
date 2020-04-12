package com.parse.ws

import com.parse.PropParser

import java.util.regex.Pattern

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/8/20, MarkHuang,new
 * </ul>
 * @since 1/8/20
 */
class WsFileParser {

    def propParser = new PropParser()

    WsFileInfo parse(File wsFile) {
        parse(wsFile.text)
    }

    WsFileInfo parse(String text) {
        def lines = text.split('\n|\r\n')

        def tasks = []
        def wsFileInfo = new WsFileInfo(tasks: tasks)

        def temp = []
        def nowTask = null
        def lastTaskLine = -1
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

    private void parseTask(String nowTask, List<String> taskContentLines, WsFileInfo wsFileInfo, List tasks, int lastTaskLine) {
        if (nowTask != null) {
            def taskContent = propParser.parseString(taskContentLines.join('\n'))
            if (nowTask == 'NAME') {
                wsFileInfo.name = taskContent
            } else if (nowTask == 'DEF') {
                int idx = taskContent.indexOf('=')
                propParser.updateVarMap(taskContent[0..idx - 1].trim(), taskContent[idx + 1..-1])
            } else if (nowTask == 'SSH_URL') {
                wsFileInfo.sshUrl = SshUrl.valueOf(taskContent)
            } else if (nowTask == 'SCP' || nowTask == 'RUN' || nowTask == 'SSH_RUN' || nowTask == 'USER') {
                tasks << new Task(type: TaskType.valueOf(nowTask), content: taskContent)
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
}





