package com.app
import com.cmd.ShellCommandRunner

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/7/19, MarkHuang,new
 * </ul>
 * @since 10/7/19
 */
class WsAdmin {

    def cmdRunner
    def hostName
    def userName
    def pwd

    WsAdmin(String wsAdminDir,String hostName,String userName,String pwd) {
        this.cmdRunner = new ShellCommandRunner(new File(wsAdminDir + File.separator + 'bin'))
        this.hostName = hostName
        this.userName = userName
        this.pwd = pwd
    }

    String getWsAdminCmd(cmd) {
        "/opt/IBM/WebSphere/AppServer/bin/wsadmin.sh -lang jython -c \"${cmd}\" -conntype SOAP -host ${hostName} -user ${userName} -password ${pwd}"
    }

    boolean checkIsAppStart(appName) {
        def cmd = "print AdminControl.completeObjectName('type=Application,name=${appName},*')"
        cmd = getWsAdminCmd(cmd)
        def scr = new ShellCommandRunner()

        def result = scr.runCommend(cmd)
        return true
    }

    void stopApp() {
    }
}
