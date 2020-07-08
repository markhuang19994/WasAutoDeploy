package com

import com.app.ScpHelper
import com.cmd.CommandRunner
import com.cmd.CommandRunnerFactory
import com.cmd.CommandSetting
import com.cmd.SshCommandRunner
import com.cmd.condition.ConditionOutput
import com.parse.PropParser
import com.parse.ws.SshUrl
import com.parse.ws.TaskType
import com.parse.ws.WsFileParser
import com.project.Project
import com.sql.DefaultSqlProcessor
import com.sql.DockerSqlProcessor
import com.sql.SqlCmdConfig
import com.util.FileUtil

import java.nio.file.Paths
import java.text.SimpleDateFormat

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
    static CommandSetting cs = new CommandSetting()

    static void main(String[] args) {
        cs.exitcodeHandler = { it == 0 }
        init(args)
        execSqlScripts()

        execWsFile()
        println '\nDeploy app on websphere\n'
        installAppOnLinuxBySsh()
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
        properties2.load(new ByteArrayInputStream(new PropParser().parseString(projectConf.text).getBytes()))
        project = new Project(new HashMap<>(properties2))
    }

    static execSqlScripts() {
        if (project.sqlDir) {
            println '\nExecute sql scripts'
            println 'sql dir:' + project.sqlDir
            def dateFormat = new SimpleDateFormat('yyyyMMdd')

            def scriptFiles = new File(project.sqlDir)
                    .listFiles({ it.name.startsWith('DeployUAT') && it.name.endsWith('.sql') } as FileFilter)
                    ?.sort { f1, f2 ->
                        Date d1 = dateFormat.parse(f1.name.replaceAll('DeployUAT(.*)\\.sql', '$1'))
                        Date d2 = dateFormat.parse(f2.name.replaceAll('DeployUAT(.*)\\.sql', '$1'))
                        Long.compare(d1.time, d2.time)
                    } ?: new File[0]

            def scriptFileList = scriptFiles.toList()
            println "scriptFiles: ${scriptFileList}"
            if (scriptFileList.size() == 0) {
                println 'Sql script file not found, skip...'
                return
            }

            def sqlCmdConfig = new SqlCmdConfig(
                    host: 'sssrv01.iead.local',
                    port: '3433',
                    user: 'sa',
                    password: 'p@ssw0rd',
                    database: 'XCOLA'
            )

            def isSqlExecOk = false

            def dockerSqlProcessor = new DockerSqlProcessor(sqlCmdConfig)
            if (dockerSqlProcessor.hasSqlCmd) {
                int exitCode = dockerSqlProcessor.executeSqlScripts(scriptFileList)
                isSqlExecOk = exitCode == 0
            }

            if (!isSqlExecOk) {
                def sqlProcessor = new DefaultSqlProcessor()
                sqlProcessor.executeSqlScripts(scriptFileList)
            }
        }
    }

    static execWsFile() {
        List<String> extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def cr = CommandRunnerFactory.getCommendRunner(null, extraPath, null, null)

        def execWsFiles = (project.wsFiles ?: '').toString().split('\\|')
        if (execWsFiles.size() == 0) return

        def wsDir = Paths.get(mainArgs.projectConfPath).resolve("../ws").normalize().toFile()
        if (!wsDir.exists()) {
            throw new RuntimeException('ws file dir not found: ' + wsDir.absolutePath)
        }

        def wsFileInfos = []
        def wsFileParser = new WsFileParser()

        for (execWsFile in execWsFiles) {
            def wsFile = new File(wsDir, execWsFile)
            if (!wsFile.exists()) {
                throw new RuntimeException('ws file not found: ' + wsFile.absolutePath)
            }

            wsFileInfos << wsFileParser.parse(wsFile)
        }

        for (wsFileInfo in wsFileInfos) {
            try {
                println "Exec:$wsFileInfo.name"

                def sshUrl = wsFileInfo.sshUrl
                def sshCmdRunner = new SshCommandRunner(cr, sshUrl)
                def scpH = new ScpHelper(sshCmdRunner, sshUrl)

                for (task in wsFileInfo.tasks) {
                    if (task.type == TaskType.SSH_RUN) {
                        sshCmdRunner.runCommend(task.content, cs)
                    } else if (task.type == TaskType.RUN) {
                        cr.runCommend(task.content, cs)
                    } else if (task.type == TaskType.SCP) {
                        def attrs = WsFileParser.parseScp(task.content)
                        scpH.cpWithAutoCreateDir(
                                attrs['target'], attrs['dest'], attrs['user'], attrs['owner'], attrs['permission'])
                    } else if (task.type == TaskType.USER) {
                        sshUrl.user = task.content
                    }
                }
                println "$wsFileInfo.name running success.\n"
            } catch (Exception e) {
                e.printStackTrace()
                println "$wsFileInfo.name running fail.\n"
            }
        }
    }

    static installAppOnLinuxBySsh() {
        def linuxTempDirPath = "/tmp/${UUID.randomUUID().toString().replace('-', '')}"

        def linuxConfigDirPath = "${linuxTempDirPath}/config"
        def linuxScriptDirPath = "${linuxTempDirPath}/script"
        def linuxWarPath = "${linuxTempDirPath}/war/${new File(mainArgs.warPath).getName()}"

        def deployScript = FileUtil.getResource('/script/deployApp.py')
        def utilScript = FileUtil.getResource('/script/application_util.py')
        def extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def cr = CommandRunnerFactory.getCommendRunner(null, extraPath, null, null)

        def sshUrl = SshUrl.valueOf(prop['ssh.url'] as String)
        def scr = new SshCommandRunner(cr, sshUrl)
        def scpH = new ScpHelper(scr, sshUrl)
        scpH.cpWithAutoCreateDir(mainArgs.warPath, linuxWarPath)
        scpH.cpWithAutoCreateDir(mainArgs.projectConfPath, "${linuxConfigDirPath}/${new File(mainArgs.projectConfPath).getName()}")
        scpH.cpWithAutoCreateDir(deployScript.absolutePath, "${linuxScriptDirPath}/${deployScript.getName()}")
        scpH.cpWithAutoCreateDir(utilScript.absolutePath, "${linuxScriptDirPath}/${utilScript.getName()}")

        scr.runCommend(
                "${prop['linux.wsadmin.path']} " +
                        "-lang jython " +
                        "-javaoption \"-Dpython.path=${linuxScriptDirPath}\" " +
                        "-conntype SOAP " +
                        "-user ${prop['wsadmin.user.name']} " +
                        "-password ${prop['wsadmin.user.pwd']} " +
                        "-f ${linuxScriptDirPath}/deployApp.py ${linuxConfigDirPath} ${linuxWarPath}"
                , cs)

        scr.runCommend("rm -rf ${linuxTempDirPath}", cs)
    }

    static genSshKey(CommandRunner cr) {
        //cat C:/Windows/system32/config/systemprofile/.ssh/id_rsa.pub | ssh root@192.168.36.91 "cat >> ~/.ssh/authorized_keys"
        //git bash ssh-copy-id root@192.168.36.92
        //echo StrictHostKeyChecking no > C:/Windows/system32/config/systemprofile/.ssh/config
        def co = new ConditionOutput('\n', { consoleLines ->
            if (consoleLines.size() < 1) return false
            return consoleLines[consoleLines.size() - 1].contains('Enter file in which')
        })

        def co2 = new ConditionOutput('y\n', { consoleLines ->
            if (consoleLines.size() < 1) return false
            return consoleLines[consoleLines.size() - 1].contains('Overwrite (y/n)')
        })
        cr.runCommend('ssh-keygen', new CommandSetting(conditionOutputList: [co, co2]))
    }

}
