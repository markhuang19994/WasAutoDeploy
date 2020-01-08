package com.cmd.ssh

import com.cmd.CommendRunner
import com.cmd.ProcessResult
import com.cmd.condition.ConditionOutput
import com.util.FileUtil

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/8/20, MarkHuang,new
 * </ul>
 * @since 1/8/20
 */
class SshCommandRunner {
    String url
    String port
    CommendRunner cr

    SshCommandRunner(CommendRunner cr, String url, String port) {
        this.url = url
        this.port = port
        this.cr = cr
    }

    ProcessResult runCommend(String cmd, Boolean printDebugMsg = true,
                             Map env = [:], List<ConditionOutput> conditionOutputList = []) {
        File tempSh = FileUtil.generateTempFile()
        tempSh << cmd

        println "cmd:${cmd}"
        def result = cr.runCommend("cat ${tempSh.absolutePath} | ssh $url -p $port 'bash -'", printDebugMsg, env, conditionOutputList)
        tempSh.delete()
        result
    }

}
