package com.cmd.helper

import com.cmd.condition.ConditionOutput

import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/9/19, MarkHuang,new
 * </ul>
 * @since 10/9/19
 */
class ConsoleHelperImpl implements ConsoleHelper{
    static final CONSOLE_ENCODING = System.properties['file.encoding'] as String ?: 'utf-8'
    private def printLog = true

    private ExecutorService executorService = Executors.newFixedThreadPool(2, { r ->
        Thread t = Executors.defaultThreadFactory().newThread(r)
        t.setDaemon(true)
        return t
    })

    String[] processConsole(Process process, List<ConditionOutput> conditionOutputList) {
        CountDownLatch cdl = new CountDownLatch(2)
        Future<String> successFuture = executorService.submit({
            String result = ''
            Reader successReader
            Writer outputWriter
            try {
                successReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), CONSOLE_ENCODING))
                outputWriter =
                        new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))
                result = readSuccessConsoleAndWriteOutput(successReader, outputWriter, conditionOutputList)
                successReader.close()
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                if (successReader != null) {
                    successReader.close()
                }
                if (outputWriter != null) {
                    outputWriter.close()
                }
                cdl.countDown()
            }
            return result
        } as Callable)

        Future<String> errFuture = executorService.submit({
            String result = ''
            Reader errorReader
            try {
                errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), CONSOLE_ENCODING))
                result = readErrorConsole(errorReader)
                errorReader.close()
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                if (errorReader != null) {
                    errorReader.close()
                }
                cdl.countDown()
            }
            return result
        } as Callable)

        cdl.await(120, TimeUnit.SECONDS)
        return [successFuture.get() ?: '', errFuture.get() ?: '']
    }

    private static void writeOutput(Writer outputWriter, List<String> lines,
                                    List<ConditionOutput> conditionOutputList) {
        try {
            if (conditionOutputList && conditionOutputList.size() > 0) {
                def conditionOutput = conditionOutputList.get(0)
                if (conditionOutput.test(lines)) {
                    outputWriter.write(conditionOutput.outputStr + System.lineSeparator())
                    outputWriter.flush()
                    conditionOutputList.remove(0)
                    println "OutPut >>> " + conditionOutput.outputStr
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    private String readSuccessConsoleWithoutLineSpAndWriteOutput(
            Reader reader, Writer outputWriter, List<ConditionOutput> conditionOutputList) {
        List<String> lines = []
        boolean firstPrint = true
        int idx = 0
        int temp
        writeOutput(outputWriter, lines, conditionOutputList)
        while ((temp = reader.read()) != -1) {
            if ((char) temp == '\n'){
                idx++
            }
            lines[idx] += String.valueOf((char) temp)
            writeOutput(outputWriter, lines, conditionOutputList)
            if (printLog) {
                if (firstPrint) {
                    println '\n\033[32mSuccessConsole:\033[0m\n'
                    firstPrint = false
                }
                print ((char) temp)
            }
        }
        return lines.join(System.lineSeparator())
    }

    private String readSuccessConsoleAndWriteOutput(Reader reader, Writer outputWriter
                                                    , List<ConditionOutput> conditionOutputList) {
        List<String> lines = []
        boolean firstPrint = true
        String thisLine
        writeOutput(outputWriter, lines, conditionOutputList)
        while ((thisLine = reader.readLine()) != null) {
            lines << thisLine
            writeOutput(outputWriter, lines, conditionOutputList)
            if (printLog) {
                if (firstPrint) {
                    println '\n\033[32mSuccessConsole:\033[0m'
                    firstPrint = false
                }
                println thisLine
            }
        }
        return lines.join(System.lineSeparator())
    }

    private String readErrorConsole(Reader reader) {
        List<String> lines = []
        boolean firstPrint = true
        String thisLine
        while ((thisLine = reader.readLine()) != null) {
            lines << thisLine
            if (printLog) {
                if (firstPrint) {
                    println '\n\033[31mErrorConsole:\033[0m'
                    firstPrint = false
                }
                println thisLine
            }
        }
        return lines.join(System.lineSeparator())
    }
}