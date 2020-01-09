package com

import com.analysis.ActionAnalysis

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/14/19, MarkHuang,new
 * </ul>
 * @since 10/14/19
 */
class MainArgsParser {
    static MainArgs parseMainArgs(String[] args) {
        def actionAnalysis = ActionAnalysis.getInstance(args)
        def errorMessageList = []

        def resourcesDirPath = actionAnalysis
                .getActionByFlag('-resources')
                .orElseGet { errorMessageList << 'resources not found' }

        def warPath = actionAnalysis
                .getActionByFlag('-warPath')
                .orElseGet { errorMessageList << 'warPath not found' }

        def projectConfPath = actionAnalysis
                .getActionByFlag('-projectConfPath')
                .orElseGet { errorMessageList << 'projectConfPath not found' }

        if (errorMessageList.size() > 0) {
            throw new RuntimeException('\n' + errorMessageList.join('\n'))
        }

        new MainArgs(
                warPath: warPath,
                resourcesDirPath: resourcesDirPath,
                projectConfPath: projectConfPath
        )
    }
}
