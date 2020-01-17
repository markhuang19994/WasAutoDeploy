package com.cmd

import com.cmd.condition.ConditionOutput
import com.cmd.helper.ConsoleHelper
import com.cmd.helper.ConsoleHelperImpl
import com.util.FileUtil

class ShellCommendRunner extends CommendRunner {

    private File runCommendDirectory
    private String defaultPATH
    private Map defaultExtraEnv

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

        run(commend, pb, conditionOutputList, printDebugMsg, execFile)
    }

    Process runCommendLight(String commend, Map env = [:]) {
        File execFile = generateExecFile(commend, env)
        ProcessBuilder pb = new ProcessBuilder(['sh', execFile.absolutePath])
        if (runCommendDirectory != null) {
            pb.directory(runCommendDirectory)
        }
        pb.start()
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

