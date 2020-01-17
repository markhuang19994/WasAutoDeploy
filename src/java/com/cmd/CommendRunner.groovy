package com.cmd

import com.cmd.condition.ConditionOutput
import com.cmd.helper.ConsoleHelper

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/9/19, MarkHuang,new
 * </ul>
 * @since 10/9/19
 */
abstract class CommendRunner {
    ConsoleHelper consoleHelper

    ProcessResult runCommend(String commend, Boolean printDebugMsg = true,
                             Map env = [:], List<ConditionOutput> conditionOutputList = []){
        throw new UnsupportedOperationException()
    }

    Process runCommendLight(String commend, Map env = [:]) {
        throw new UnsupportedOperationException()
    }

    ProcessResult run(String commend, ProcessBuilder pb, List<ConditionOutput> conditionOutputList, boolean printDebugMsg, File execFile) {
        println "Run command:\033[33m${commend}\033[0m"
        Process process = pb.start()
        String[] console = consoleHelper.processConsole(process, conditionOutputList)

        int exitCode = process.waitFor()
        println "\033[34mExit code:$exitCode\n\033[0m"

        if (exitCode != 0 && printDebugMsg) {
            println "\033[31m[Debug cmd]\n$execFile.text\033[0m\n"
        }
        execFile.deleteOnExit()

        return new ProcessResult(
                successConsole: console[0].replace('\r\n', '\n'),
                errorConsole: console[1].replace('\r\n', '\n'),
                exitCode: exitCode
        )
    }
}
