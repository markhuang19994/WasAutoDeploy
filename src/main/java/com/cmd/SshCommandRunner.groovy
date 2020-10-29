package com.cmd


import com.util.FileUtil

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/8/20, MarkHuang,new
 * </ul>
 * @since 1/8/20
 */
class SshCommandRunner implements CommandRunner {
    SshUrl sshUrl
    CommandRunner commandRunner

    SshCommandRunner(CommandRunner commandRunner, SshUrl sshUrl) {
        this.sshUrl = sshUrl
        this.commandRunner = commandRunner
    }

    ProcessResult runCommend(String cmd, CommandSetting commendSetting) {
        File tempSh = FileUtil.generateTempFile()
        tempSh << cmd
        println "cmd:${cmd}"

        def result = commandRunner.runCommend("cat ${tempSh.absolutePath} | ssh ${sshUrl.fullUrl()} -p $sshUrl.port 'bash -'", commendSetting)
        tempSh.delete()
        result
    }

}
