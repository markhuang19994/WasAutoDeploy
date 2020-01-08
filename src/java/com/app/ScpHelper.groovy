package com.app

import com.cmd.CommendRunner
import com.cmd.ssh.SshCommandRunner

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/7/20, MarkHuang,new
 * </ul>
 * @since 1/7/20
 */
class ScpHelper {

    CommendRunner cr
    String ssh = ''
    String port = ''

    ScpHelper(CommendRunner cr, String ssh, String port = '22') {
        this.cr = cr
        this.ssh = ssh
        this.port = port
    }

    void cpWithAutoCreateDir(target, dest, user = null, owner = null, permission = null) {
        def sshCmdRunner = new SshCommandRunner(cr, ssh, port)
        def scpCmd = "scp -P ${port}"

        def targetFile = new File(target)
        def chownMod = []
        if (user || owner) {
            chownMod << "chown -R ${user ?: ''}:${owner ?: ''} @dest"
        }
        if (permission) {
            chownMod << 'chmod -R ' + permission + ' @dest'
        }

        def ownModCmd = chownMod.size() > 0
                ? chownMod.join(' && ')
                : ''

        def destFile = new File(dest)
        def outerDirPath = targetFile.isDirectory()
                ? destFile.absolutePath
                : destFile.parentFile.absolutePath

        sshCmdRunner.runCommend(
                "[ ! -d $outerDirPath ] && mkdir -p $outerDirPath && " +
                        ownModCmd.replace('@dest', outerDirPath) +
                        ' || true'
        )

        cr.runCommend("${scpCmd} -r ${target} ${ssh}:${dest}")
        if (ownModCmd != '') {
            sshCmdRunner.runCommend(" ${ownModCmd.replace('@dest', dest)}")
        }
    }
}
