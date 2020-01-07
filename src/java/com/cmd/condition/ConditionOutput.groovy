package com.cmd.condition

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/7/19, MarkHuang,new
 * </ul>
 * @since 10/7/19
 */
class ConditionOutput {

    List<CmdCondition> cmdConditions = []
    def outputStr = ''

    ConditionOutput(String outputStr) {
        this.outputStr = outputStr
    }

    void addCmdCondition(CmdCondition cmdCondition) {
        cmdConditions << cmdCondition
    }

    boolean test(List<String> consoleLines) {
        cmdConditions.any { it.test(consoleLines) }
    }
}
