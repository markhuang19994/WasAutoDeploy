package com.sql

import com.cmd.BatCommandRunner
import com.cmd.CommandRunnerFactory
import com.cmd.CommandSetting
import com.util.FileUtil

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class DockerSqlProcessor {

    static void main(String[] args) {
        print 'exitCode:' + new DockerSqlProcessor(new SqlCmdConfig(
                host: 'sssrv01.iead.local',
                port: '3433',
                user: 'sa',
                password: 'p@ssw0rd',
                database: 'XCOLA',
        )).executeSqlScripts([
                '/home/markhuag/Downloads/DeployUAT20200320 (2).sql' as File,
                '/home/markhuag/Downloads/DeployUAT20200320 (1).sql' as File
        ])
    }

    def hasSqlCmd = false
    SqlCmdConfig sqlCmdConfig

    DockerSqlProcessor(SqlCmdConfig sqlCmdConfig) {
        try {
            def dockerCmd = ['docker', 'image', 'ls', '--format', '{{.Repository}}']
            def processResult = runDockerCmd(dockerCmd)
            if (processResult.exitCode == 0) {
                def images = processResult.successConsole
                this.hasSqlCmd = images.split('\r?\n').any { it == 'sqlcmd' }
            } else {
                println processResult.errorConsole
            }
            this.sqlCmdConfig = sqlCmdConfig
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    int executeSqlScripts(List<File> scriptFiles) {
        if (!hasSqlCmd) {
            println 'SqlCmd image not found in docker, skip...'
            return 1
        }

        def tempDir = FileUtil.generateTempDir()

        try {
            def instructTxt = new File(tempDir, 'instruct.txt')

            instructTxt.withWriter {
                for (scriptFile in scriptFiles) {
                    def tempScriptFile = copyFileToTmpDir(scriptFile, tempDir)
                    def scriptBytes = Files.readAllBytes(tempScriptFile.toPath())
                    def scriptStr = new String(scriptBytes, StandardCharsets.UTF_16BE)
                    scriptStr = scriptStr.startsWith('\ufeff') ? scriptStr[1..-1] : scriptStr

                    def transactionScript = "BEGIN TRANSACTION;\n${scriptStr}\nCOMMIT TRANSACTION;\nPRINT N'Transaction success';"
                    tempScriptFile.withOutputStream {
                        //BOM \ufffe is necessary for sqlcmd, if not insert BOM, sqlcmd will not execute script!
                        it.write(new byte[]{0xFE, 0xFF})
                        it.write(transactionScript.getBytes(StandardCharsets.UTF_16BE))
                        it.flush()
                    }
                    it.write(tempScriptFile.name)
                    it.write('\n')
                }
                it.flush()
            }

            def dockerTempDirPath = "/tmp/${tempDir.name}"
            def tempDirPath = CommandRunnerFactory.IS_WINDOWS ? getDockerTempPath(tempDir.name) : tempDir.absolutePath
            def dockerCmd = [
                    'docker', 'run', '--rm',
                    '-v', "${tempDirPath}:${dockerTempDirPath}",
                    'sqlcmd:latest', "/usr/script/sqlcmd_run_file.sh",
                    dockerTempDirPath,
                    sqlCmdConfig.host, sqlCmdConfig.port,
                    sqlCmdConfig.user, sqlCmdConfig.password, sqlCmdConfig.database
            ]

            def processResult = runDockerCmd(dockerCmd)
            return processResult.exitCode == 0 ? 0 : 1
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
//            FileUtil.deleteDirectory(tempDir)
        }
        return 1;
    }

    private static runDockerCmd(List<String> dockerCmd) {
        def cr = CommandRunnerFactory.getCommendRunner(null)


        if (cr instanceof BatCommandRunner) {
            cr.defaultPATH += File.pathSeparator + 'C:\\Program Files\\Docker Toolbox'
            cr.runCommend("${dockerCmd.join(' ')}", new CommandSetting(env: [
                    DOCKER_TLS_VERIFY            : '1',
                    DOCKER_HOST                  : 'tcp://192.168.99.100:2376',
                    DOCKER_CERT_PATH             : 'C:\\Users\\iisi\\.docker\\machine\\machines\\default',
                    DOCKER_MACHINE_NAME          : 'default',
                    COMPOSE_CONVERT_WINDOWS_PATHS: 'true'
            ], consoleEncoding: 'utf-8'))
        }
    }

    private static File copyFileToTmpDir(File f, File tmpDir) {
        def cpf = new File(tmpDir, f.name)

        def bytes = Files.readAllBytes(f.toPath())

        cpf.withOutputStream {
            it.write(bytes)
            it.flush()
        }
        cpf
    }

    private static String getDockerTempPath(String dirName) {
        "//c/windows/temp/${dirName}"
    }

}
