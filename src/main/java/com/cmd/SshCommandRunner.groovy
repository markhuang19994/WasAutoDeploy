package com.cmd

import com.cmd.CommendRunner
import com.cmd.ProcessResult
import com.parse.ws.SshUrl
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

    ProcessResult runCommend(String beforeCmd, String cmd, CommendSetting commendSetting) {
        File tempSh = FileUtil.generateTempFile()
        if (beforeCmd) {
            tempSh << beforeCmd
        }
        tempSh << cmd
        println "cmd:${cmd}"

        def result = commandRunner.runCommend("cat ${tempSh.absolutePath} | ssh ${sshUrl.fullUrl()} -p $sshUrl.port 'bash -'", commendSetting)
        tempSh.delete()
        result
    }

    ProcessResult runCommend(String cmd, CommendSetting commendSetting) {
        runCommend(null, cmd, commendSetting)
    }

}
