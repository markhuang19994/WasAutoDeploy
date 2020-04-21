package com.cmd.helper

import com.cmd.condition.ConditionOutput

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/9/19, MarkHuang,new
 * </ul>
 * @since 10/9/19
 */
interface ConsoleHelper {
    String[] processConsole(Process process, List<ConditionOutput> conditionOutputList, String consoleEncoding)
}
