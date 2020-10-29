package com.cmd


import com.cmd.helper.ConsoleHelper
import com.cmd.helper.ConsoleHelperImpl

import javax.naming.OperationNotSupportedException

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/9/19, MarkHuang,new
 * </ul>
 * @since 10/9/19
 */
class CommandRunnerFactory {
    static final boolean IS_WINDOWS = System.properties['os.name'].toString().toLowerCase().contains('window')

    static CommandRunner getCommandRunner(File runCommendDirectory = null, List<String> extraPath = null,
                                          Map<String, String> extraEnv = null, ConsoleHelper consoleHelper = null) {
        runCommendDirectory = runCommendDirectory ?: new File(System.getProperty('user.dir'))
        extraPath = extraPath ?: []
        extraEnv = extraEnv ?: [:]
        consoleHelper = consoleHelper ?: new ConsoleHelperImpl()
        IS_WINDOWS
                ? new BatCommandRunner(runCommendDirectory, extraPath, extraEnv, consoleHelper)
                : new ShellCommandRunner(runCommendDirectory, extraPath, extraEnv, consoleHelper)
    }

    static CommandRunner getRemoteCommandRunner(SshUrl sshUrl, File runCommendDirectory = null, List<String> extraPath = null,
                                                Map<String, String> extraEnv = null, ConsoleHelper consoleHelper = null) {

        if (IS_WINDOWS) {
            throw new OperationNotSupportedException()
        }
        new SshCommandRunner(getCommandRunner(runCommendDirectory, extraPath, extraEnv, consoleHelper), sshUrl)
    }
}
