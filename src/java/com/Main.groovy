package com

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
        testCmd()
//        installOnLinuxBySsh()
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

    static testCmd() {
        def ssh = prop['ssh.url']
        def updateScript = FileUtil.getResource('/script/test.py')
        def updateScript2 = FileUtil.getResource('/script/test2.py')
        def linuxScriptPath = prop['linux.script.path']
        List<String> extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def scr = CommendRunnerFactory.getCommendRunner(null, extraPath, null, null)

        scr.runCommend("scp ${updateScript.absolutePath} ${ssh}:${linuxScriptPath}")
        scr.runCommend("scp ${updateScript2.absolutePath} ${ssh}:${linuxScriptPath}")
        scr.runCommend(
                "ssh ${ssh} ${prop['linux.wsadmin.path']} " +
                        "-lang jython -conntype SOAP " +
                        "-user ${prop['wsadmin.user.name']} " +
                        "-password ${prop['wsadmin.user.pwd']} " +
                        "-f ${linuxScriptPath}/test.py"
        )
    }

    static installOnLinuxBySsh() {
        def ssh = prop['ssh.url']
        def updateScript = FileUtil.getResource('/script/updateApp.py')
        def linuxScriptPath = prop['linux.script.path']
        List<String> extraPath = (prop['cmd.extra.path'] as String)?.split(',')?.toList() ?: []
        def scr = CommendRunnerFactory.getCommendRunner(null, extraPath, null, null)
        scr.runCommend("scp -r ${mainArgs.dPath}/* ${ssh}:${prop['linux.d.path']}")
        scr.runCommend("scp ${mainArgs.warPath} ${ssh}:${project.linuxWarPath}")
        scr.runCommend("scp ${updateScript.absolutePath} ${ssh}:${linuxScriptPath}")
        scr.runCommend(
                "ssh ${ssh} ${prop['linux.wsadmin.path']} " +
                        "-lang jython -conntype SOAP " +
                        "-user ${prop['wsadmin.user.name']} " +
                        "-password ${prop['wsadmin.user.pwd']} " +
                        "-f ${linuxScriptPath}/updateApp.py ${project.wsAppName} ${project.wsNodeName} ${project.linuxWarPath}"
        )
//        /apps/was-apps/cola/cola_Runtime/bin/wsadmin.sh -lang jython -conntype SOAP -user wasadmin password wasadmin
//        scr.runCommend("scp -r /home/markhuag/Desktop/z/pcl4/D/* root@192.168.36.88:/mnt/sharedata")
//        scr.runCommend("scp    /home/markhuag/Desktop/z/pcl4/extfunc05.war root@192.168.36.88:/mnt/sharedata/other/was/war/extfunc05.war")
//        scr.runCommend("scp    /home/markhuag/Documents/project/source/Learn/WasAutoDeploy/src/resources/script/updateApp.py root@192.168.36.88:/mnt/sharedata/other/was/script/updateApp.py")
//        scr.runCommend("ssh root@192.168.36.88 /opt/IBM/WASV8.5/bin/wsadmin.sh -lang jython -conntype SOAP -user Mark -f /mnt/sharedata/other/was/script/updateApp.py extfunc05_war finsrv02Node01 /mnt/sharedata/other/was/war/extfunc05.war")
    }

    static void updateLinuxApp() {
        def wsadminPath = '/opt/IBM/WebSphere/AppServer/bin/wsadmin.sh'
        def updateScriptPath = '/usr/resources/wsadmin/script'
        def appWarPath = ['/home/markhuag/Desktop/extfunc04.war', '/home/was/resources/pcl3/extfunc04.war']
        def appDPath = ['/home/markhuag/Desktop/D/*', '/home/was/sharedata']
        def appName = 'extfunc04_war'
        def nodeName = 'DefaultNode01'
        def hostName = 'localhost'
        def userName = 'wsadmin'
        def password = 'zU9tfSUh'
        def updateApp = FileUtil.getResource('/script/updateApp.py')
        def scr = new ShellCommendRunner()
        scr.runCommend("docker cp ${updateApp.absolutePath} was1:/usr/resources/wsadmin/script")
        scr.runCommend("docker cp ${appWarPath[0]} was1:${appWarPath[1]}")
        scr.runCommend("docker cp ${appDPath[0]} was1:${appDPath[1]}")
        scr.runCommend("docker exec -i --user root was1 chown -R was:root ${appDPath[1]}")
        scr.runCommend(String.format("docker exec -i was1 %s",
                "${wsadminPath} " +
                        "-lang jython -conntype SOAP -host $hostName -user $userName -password $password " +
                        "-f ${updateScriptPath}/updateApp.py $appName $nodeName ${appWarPath[1]}"
        ))
    }


    static void update() {
        def wsadminPath = '/opt/IBM/WebSphere/AppServer/bin/wsadmin.sh'
        def updateScriptPath = '/usr/resources/wsadmin/script'
        def appWarPath = ['/home/markhuag/Desktop/extfunc04.war', '/home/was/resources/pcl3/extfunc04.war']
        def appDPath = ['/home/markhuag/Desktop/D/*', '/home/was/sharedata']
        def appName = 'extfunc04_war'
        def nodeName = 'DefaultNode01'
        def hostName = 'localhost'
        def userName = 'wsadmin'
        def password = 'zU9tfSUh'
        def updateApp = FileUtil.getResource('/script/updateApp.py')
        def scr = new ShellCommendRunner()
        scr.runCommend("docker cp ${updateApp.absolutePath} was1:/usr/resources/wsadmin/script")
        scr.runCommend("docker cp ${appWarPath[0]} was1:${appWarPath[1]}")
        scr.runCommend("docker cp ${appDPath[0]} was1:${appDPath[1]}")
        scr.runCommend("docker exec -i --user root was1 chown -R was:root ${appDPath[1]}")
        scr.runCommend(String.format("docker exec -i was1 %s",
                "${wsadminPath} " +
                        "-lang jython -conntype SOAP -host $hostName -user $userName -password $password " +
                        "-f ${updateScriptPath}/updateApp.py $appName $nodeName ${appWarPath[1]}"
        ))
    }

    static void install() {
        def wsadminPath = '/opt/IBM/WebSphere/AppServer/bin/wsadmin.sh'
        def scriptPath = '/usr/resources/wsadmin/script'
        def appWarPath = ['/home/markhuag/Desktop/z/pcl4/extfunc05.war', '/home/was/resources/pcl4/extfunc05.war']
        def appDPath = ['/home/markhuag/Desktop/z/pcl4/D/*', '/home/was/sharedata']
        def appName = 'extfunc05_war'
        def nodeName = 'DefaultNode01'
        def hostName = 'localhost'
        def userName = 'wsadmin'
        def password = 'zU9tfSUh'
        def serverName = 'server1'
        def contextPath = '/extfunc05'
        def installApp = FileUtil.getResource('/script/installApp.py')
        def scr = new ShellCommendRunner()
        scr.runCommend("docker cp ${installApp.absolutePath} was1:/usr/resources/wsadmin/script")
        scr.runCommend("docker cp ${appWarPath[0]} was1:${appWarPath[1]}")
        scr.runCommend("docker cp ${appDPath[0]} was1:${appDPath[1]}")
        scr.runCommend("docker exec -i --user root was1 chown -R was:root ${appDPath[1]}")
        scr.runCommend(String.format("docker exec -i was1 %s",
                "${wsadminPath} " +
                        "-lang jython -conntype SOAP -host $hostName -user $userName -password $password " +
                        "-f ${scriptPath}/installApp.py $serverName $appName $nodeName $contextPath ${appWarPath[1]}"
        ))
    }


}
