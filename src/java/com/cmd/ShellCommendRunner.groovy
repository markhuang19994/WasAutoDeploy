package com.cmd

import com.cmd.condition.ConditionOutput
import com.cmd.helper.ConsoleHelper
import com.cmd.helper.ConsoleHelperImpl
import com.util.FileUtil

class ShellCommendRunner extends CommendRunner {

    private File runCommendDirectory
    private String defaultPATH
    private Map defaultExtraEnv
    private ConsoleHelper consoleHelper

    ShellCommendRunner(File runCommendDirectory, List<String> extraPath,
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
        ProcessBuilder pb = new ProcessBuilder(['sh', execFile.absolutePath])
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
                successConsole: console[0],
                errorConsole: console[1],
                exitCode: exitCode
        )
    }

    File generateExecFile(String commend, Map env) {
        def tempExecFile = FileUtil.generateTempFile()
        List<String> runtime = []

        runtime << "export PATH='$defaultPATH'".toString()

        def allEnv = [:] << defaultExtraEnv
        allEnv.putAll(env ?: [:])
        allEnv.forEach { k, v ->
            runtime << "export $k='$v'".toString()
        }

        def ls = System.lineSeparator()
        tempExecFile << '#! /bin/sh' + ls
        tempExecFile << runtime.join(ls) + ls
        tempExecFile << commend
        return tempExecFile
    }

}

