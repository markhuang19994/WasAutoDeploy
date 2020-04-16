package com.sql


import com.util.FileUtil

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
            def p = ['docker', 'image', 'ls', '--format', '{{ .Repository }}'].execute()
            p.waitFor()
            if (p.exitValue() == 0) {
                def images = p.text
                this.hasSqlCmd = images.split('\r?\n').any { it == 'sqlcmd' }
            } else {
                println p.errorStream.text
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
                    it.writeLine(copyFileToTmpDir(scriptFile, tempDir).name)
                }
                it.flush()
            }

            def dockerTempDir = "/tmp/${tempDir.name}"
            def dockerRunCmd = [
                    'docker', 'run', '--rm',
                    '-v', "${tempDir.absolutePath}:${dockerTempDir}",
                    'sqlcmd:latest', "/usr/script/sqlcmd_run_file.sh",
                    tempDir.absolutePath,
                    sqlCmdConfig.host, sqlCmdConfig.port,
                    sqlCmdConfig.user, sqlCmdConfig.password, sqlCmdConfig.database
            ]

            def process = dockerRunCmd.execute()
            process.waitFor()
            if (process.exitValue() != 0) {
                println process.errorStream.text
                return 1
            } else {
                println process.text
                return 0
            }
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            FileUtil.deleteDirectory(tempDir)
        }
        return 1;
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

}
