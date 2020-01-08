package com.app

import com.cmd.CommendRunner

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/7/20, MarkHuang,new
 * </ul>
 * @since 1/7/20
 */
class ScpHelper {

    CommendRunner cr
    String ssh

    ScpHelper(CommendRunner cr, String ssh) {
        this.cr = cr
        this.ssh = ssh
    }

    void cpWithAutoCreateDir(target, dest) {
        cr.runCommend("ssh ${ssh} mkdir -p ${new File(dest).parentFile}")
        cr.runCommend("scp -r ${target} ${ssh}:${dest}")
    }
}
