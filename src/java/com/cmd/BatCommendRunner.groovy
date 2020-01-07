package com.cmd

import com.cmd.condition.ConditionOutput
import com.cmd.helper.ConsoleHelper
import com.cmd.helper.ConsoleHelperImpl
import com.util.FileUtil

class BatCommendRunner extends CommendRunner {

    File runCommendDirectory
    String defaultPATH
    Map defaultExtraEnv
    ConsoleHelper consoleHelper

    BatCommendRunner(File runCommendDirectory, List<String> extraPath,
                     Map<String, String> extraEnv, ConsoleHelper consoleHelper) {
        this.runCommendDirectory = runCommendDirectory
        String path = System.getenv('PATH')
        defaultPATH = path + File.pathSeparator + extraPath.join(File.pathSeparator)
        defaultExtraEnv = extraEnv
        this.consoleHelper = consoleHelper
    }

    ProcessResult runCommend(String commend, Boolean printDebugMsg = true,
                             Map env = [:], List<ConditionOutput> conditionOutputList = []) {
        File execFile = generateExecFile(commend, env)
        ProcessBuilder pb = new ProcessBuilder(['cmd', '/C', execFile.absolutePath])
        if (runCommendDirectory != null) {
            pb.directory(runCommendDirectory)
        }

        println "Run command:\033[33m${commend}\033[0m"
        Process process = pb.start()
        String[] console = consoleHelper.processConsole(process, conditionOutputList)

        int exitCode = process.waitFor()
        println "\033[34mExit code:$exitCode\n\033[0m"

        if (exitCode != 0 && printDebugMsg) {
            println "\033[31m[Debug cmd]\n$execFile.text\033[0m"
        }
        execFile.deleteOnExit()

        return new ProcessResult(
                successConsole: console[0].replace('\r\n', '\n'),
                errorConsole: console[1].replace('\r\n', '\n'),
                exitCode: exitCode
        )
    }

    File generateExecFile(String commend, Map env) {
        def tempExecFile = File.createTempFile(System.currentTimeMillis() as String, '.bat')
        List<String> runtime = []

        runtime << '@echo off'
        runtime << "set PATH=$defaultPATH".toString()

        def allEnv = [:] << defaultExtraEnv
        allEnv.putAll(env ?: [:])
        allEnv.forEach { k, v ->
            runtime << "set $k=$v".toString()
        }

        tempExecFile << runtime.join('\n') + '\n'
        tempExecFile << commend
        return tempExecFile
    }

}

