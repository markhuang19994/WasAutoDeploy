package com.app

import com.analysis.ws.SshUrl
import com.cmd.ssh.SshCommandRunner

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/7/20, MarkHuang,new
 * </ul>
 * @since 1/7/20
 */
class ScpHelper {

    SshCommandRunner scr

    SshUrl sshUrl

    ScpHelper(SshCommandRunner scr, SshUrl sshUrl) {
        this.scr = scr
        this.sshUrl = sshUrl
    }

    void cpWithAutoCreateDir(target, dest, user = null, owner = null, permission = null) {
        def scpCmd = "scp -P ${sshUrl.port}"

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
        def outerDirPath = targetFile.isDirectory() || targetFile.path.matches('^.*?[\\\\/]\\*')
                ? destFile.path
                : destFile.parentFile.path

        scr.runCommend(
                "[ ! -d $outerDirPath ] && mkdir -p $outerDirPath" +
                        "${ownModCmd == '' ? '' : ' && '}" +
                        ownModCmd.replace('@dest', outerDirPath) +
                        ' || true'
        )

        scr.commandRunner.runCommend("${scpCmd} -r ${target} ${sshUrl.fullUrl()}:${dest}")
        if (ownModCmd != '') {
            scr.runCommend(" ${ownModCmd.replace('@dest', dest)}")
        }
    }
}
