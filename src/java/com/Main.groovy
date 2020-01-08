package com

import com.app.ScpHelper
import com.cmd.CommendRunnerFactory
import com.cmd.ShellCommendRunner
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

    static installOnLinuxBySsh() {
        def ssh = prop['ssh.url']
        def deployScript = FileUtil.getResource('/script/deployApp.py')
        def utilScript = FileUtil.getResource('/script/application_util.py')
        def linuxConfigPath = prop['linux.config.path']
        def linuxScriptPath = prop['linux.script.path']
        List<String> extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def cr = CommendRunnerFactory.getCommendRunner(null, extraPath, null, null)

        def scpH = new ScpHelper(cr, ssh as String)
        scpH.cpWithAutoCreateDir(mainArgs.dPath + "/*", project.linuxDPath)
        scpH.cpWithAutoCreateDir(mainArgs.warPath, project.linuxWarPath)
        scpH.cpWithAutoCreateDir(mainArgs.projectConfPath, linuxConfigPath)
        scpH.cpWithAutoCreateDir(deployScript.absolutePath, linuxScriptPath)
        scpH.cpWithAutoCreateDir(utilScript.absolutePath, linuxScriptPath)

        cr.runCommend(
                "ssh ${ssh} ${prop['linux.wsadmin.path']} " +
                        "-lang jython " +
                        "-javaoption \"-Dpython.path=${linuxScriptPath}\" " +
                        "-conntype SOAP " +
                        "-user ${prop['wsadmin.user.name']} " +
                        "-password ${prop['wsadmin.user.pwd']} " +
                        "-f ${linuxScriptPath}/deployApp.py ${linuxConfigPath}"
        )
    }

}
