package com.analysis.ws

/**
 * @author MarkHuang*
 * <ul>
 *  <li>1/8/20, MarkHuang,new
 * </ul>
 * @since 1/8/20
 */
class SshUrl {
    def url
    def user
    def port

    String fullUrl() {
        "$user@$url"
    }

    static SshUrl valueOf(String str) {
        def sshUrl = new SshUrl(port: '22')

        if (str.contains(':')) {
            def sp = str.split(':')
            str = sp[0]
            sshUrl.port = sp[1]
        }

        def sp = str.split('@')
        sshUrl.user = sp[0]
        sshUrl.url = sp[1]
        sshUrl
    }
}
