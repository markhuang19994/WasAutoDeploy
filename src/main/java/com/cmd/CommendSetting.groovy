package com.cmd

import com.cmd.condition.ConditionOutput
import groovy.transform.AutoClone

import java.util.function.Function

/**
 * @author MarkHuang*
 * <ul>
 *  <li>3/11/20, MarkHuang,new
 * </ul>
 * @since 3/11/20
 */
@AutoClone
class CommendSetting {
    Boolean printDebugMsg = true
    Map env = [:]
    List<ConditionOutput> conditionOutputList = []
    Closure exitcodeHandler = { exitcode -> return true }
}
