package com.cmd.condition

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/7/19, MarkHuang,new
 * </ul>
 * @since 10/7/19
 */
class ConditionOutput {

    CmdCondition cmdCondition
    def outputStr = ''

    ConditionOutput(String outputStr, CmdCondition cmdCondition) {
        this.outputStr = outputStr
        this.cmdCondition = cmdCondition
    }

    boolean test(List<String> consoleLines) {
        cmdCondition.test(consoleLines)
    }
}
