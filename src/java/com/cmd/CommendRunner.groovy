package com.cmd

import com.cmd.condition.ConditionOutput

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/9/19, MarkHuang,new
 * </ul>
 * @since 10/9/19
 */
abstract class CommendRunner {
    ProcessResult runCommend(String commend, Boolean printDebugMsg = true,
                             Map env = [:], List<ConditionOutput> conditionOutputList = []){
        throw new UnsupportedOperationException()
    }
}
