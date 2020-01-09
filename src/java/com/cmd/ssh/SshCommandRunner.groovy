package com.cmd.ssh

import com.analysis.ws.SshUrl
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
    SshUrl sshUrl
    CommendRunner commandRunner

    SshCommandRunner(CommendRunner commandRunner, SshUrl sshUrl) {
        this.sshUrl = sshUrl
        this.commandRunner = commandRunner
    }

    ProcessResult runCommend(String cmd, Boolean printDebugMsg = true,
                             Map env = [:], List<ConditionOutput> conditionOutputList = []) {
        File tempSh = FileUtil.generateTempFile()
        tempSh << cmd
        println "cmd:${cmd}"

        def result = commandRunner.runCommend("cat ${tempSh.absolutePath} | ssh ${sshUrl.fullUrl()} -p $sshUrl.port 'bash -'", printDebugMsg, env, conditionOutputList)
        tempSh.delete()
        result
    }

}
