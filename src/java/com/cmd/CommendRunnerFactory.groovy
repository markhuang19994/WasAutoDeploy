package com.cmd

import com.cmd.condition.ConditionOutput
import com.cmd.helper.ConsoleHelper
import com.cmd.helper.ConsoleHelperImpl

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/9/19, MarkHuang,new
 * </ul>
 * @since 10/9/19
 */
class CommendRunnerFactory {
    static final boolean IS_WINDOWS = System.properties['os.name'].toString().toLowerCase().contains('window')

    static CommendRunner getCommendRunner(File runCommendDirectory = null, List<String> extraPath = null,
                                          Map<String, String> extraEnv = null, ConsoleHelper consoleHelper = null) {
        runCommendDirectory = runCommendDirectory ?: new File(System.getProperty('user.dir'))
        extraPath = extraPath ?: []
        extraEnv = extraEnv ?: [:]
        consoleHelper = consoleHelper ?: new ConsoleHelperImpl()
        IS_WINDOWS
                ? new BatCommendRunner(runCommendDirectory, extraPath, extraEnv, consoleHelper)
                : new ShellCommendRunner(runCommendDirectory, extraPath, extraEnv, consoleHelper)
    }
}
