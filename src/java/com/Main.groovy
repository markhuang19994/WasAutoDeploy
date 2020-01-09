package com

import com.analysis.ws.SshUrl
import com.analysis.ws.TaskType
import com.analysis.ws.WsFileAnalysis
import com.analysis.ws.WsFileInfo
import com.app.ScpHelper
import com.cmd.CommendRunner
import com.cmd.CommendRunnerFactory
import com.cmd.condition.CmdCondition
import com.cmd.condition.ConditionOutput
import com.cmd.ssh.SshCommandRunner
import com.project.Project
import com.util.FileUtil

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/7/19, MarkHuang,new
 * </ul>
 * @since 10/7/19
 */
class Main {

    static def prop
    static MainArgs mainArgs
    static Project project

    static void main(String[] args) {
        init(args)
        execWsFile()
        println '\nDeploy app on websphere\n'
        installOnLinuxBySsh()
    }

    static init(String[] args) {
        //init main args
        mainArgs = MainArgsParser.parseMainArgs(args)
        FileUtil.resourcesDir = mainArgs.resourcesDirPath as File

        //init default config
        def config = FileUtil.getResource('/config/config.properties')
        def properties = new Properties()
        properties.load(new FileInputStream(config))
        prop = new HashMap<>(properties)

        //init project
        def projectConf = mainArgs.projectConfPath as File
        def properties2 = new Properties()
        properties2.load(new FileInputStream(projectConf))
        project = new Project(new HashMap<>(properties2))
    }

    static execWsFile() {
        List<String> extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def cr = CommendRunnerFactory.getCommendRunner(null, extraPath, null, null)

        def execWsFiles = (project.wsFiles ?: '').toString().split('\\|')
        if (execWsFiles.size() == 0) return

        File wsDir = new File(new File(mainArgs.projectConfPath).parentFile, 'ws')
        if (!wsDir.exists()) {
            throw new RuntimeException('ws file dir not found: ' + wsDir.absolutePath)
        }

        List<WsFileInfo> wsFileInfos = []

        for (execWsFile in execWsFiles) {
            File wsFile = new File(wsDir, execWsFile)
            if (!wsFile.exists()) {
                throw new RuntimeException('ws file not found: ' + wsFile.absolutePath)
            }

            wsFileInfos << WsFileAnalysis.parseWsFile(wsFile)
        }

        for (wsFileInfo in wsFileInfos) {
            println "Exec:$wsFileInfo.name"

            def sshUrl = wsFileInfo.sshUrl
            def sshCmdRunner = new SshCommandRunner(cr, sshUrl)
            def scpH = new ScpHelper(sshCmdRunner, sshUrl)

            for (task in wsFileInfo.tasks) {
                if (task.type == TaskType.RUN) {
                    sshCmdRunner.runCommend(task.content)
                } else if (task.type == TaskType.SCP) {
                    def attrs = WsFileAnalysis.parseScp(task.content)
                    scpH.cpWithAutoCreateDir(attrs['target'], attrs['dest'], attrs['user'], attrs['owner'], attrs['permission'])
                } else if (task.type == TaskType.USER) {
                    sshUrl.user = task.content
                }
            }
            println "$wsFileInfo.name running success.\n"
        }
    }

    static installOnLinuxBySsh() {
        def deployScript = FileUtil.getResource('/script/deployApp.py')
        def utilScript = FileUtil.getResource('/script/application_util.py')
        def linuxConfigPath = prop['linux.config.path']
        def linuxScriptPath = prop['linux.script.path']
        List<String> extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def cr = CommendRunnerFactory.getCommendRunner(null, extraPath, null, null)

        def sshUrl = SshUrl.valueOf(prop['ssh.url'] as String)
        def scr = new SshCommandRunner(cr, sshUrl)
        def scpH = new ScpHelper(scr, sshUrl)
        scpH.cpWithAutoCreateDir(mainArgs.warPath, project.linuxWarPath)
        scpH.cpWithAutoCreateDir(mainArgs.projectConfPath, linuxConfigPath + '/' + new File(mainArgs.projectConfPath).getName())
        scpH.cpWithAutoCreateDir(deployScript.absolutePath, linuxScriptPath + '/' + deployScript.getName())
        scpH.cpWithAutoCreateDir(utilScript.absolutePath, linuxScriptPath + '/' + utilScript.getName())

        scr.runCommend(
                "${prop['linux.wsadmin.path']} " +
                        "-lang jython " +
                        "-javaoption \"-Dpython.path=${linuxScriptPath}\" " +
                        "-conntype SOAP " +
                        "-user ${prop['wsadmin.user.name']} " +
                        "-password ${prop['wsadmin.user.pwd']} " +
                        "-f ${linuxScriptPath}/deployApp.py ${linuxConfigPath}"
        )
    }

    static genSshKey(CommendRunner cr) {
        //cat C:/Windows/system32/config/systemprofile/.ssh/id_rsa.pub | ssh root@192.168.36.91 "cat >> ~/.ssh/authorized_keys"
        //git bash ssh-copy-id root@192.168.36.92
        //echo StrictHostKeyChecking no > C:/Windows/system32/config/systemprofile/.ssh/config
        def co = new ConditionOutput('\n')
        co.addCmdCondition(new CmdCondition() {
            @Override
            boolean test(List<String> consoleLines) {
                if (consoleLines.size() < 1) return false
                return consoleLines[consoleLines.size() - 1].contains('Enter file in which')
            }
        })
        def co2 = new ConditionOutput('y\n')
        co2.addCmdCondition(new CmdCondition() {
            @Override
            boolean test(List<String> consoleLines) {
                if (consoleLines.size() < 1) return false
                return consoleLines[consoleLines.size() - 1].contains('Overwrite (y/n)')
            }
        })
        cr.runCommend('ssh-keygen', true, null, [co, co2])
    }

}
