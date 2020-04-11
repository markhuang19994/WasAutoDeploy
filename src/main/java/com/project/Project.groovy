package com.project

/**
 * @author MarkHuang* @version
 * <ul>
 *  <li>10/14/19, MarkHuang,new
 * </ul>
 * @since 10/14/19
 */
class Project {

    String name
    String wsAppName
    String wsNodeName
    String wsFiles
    String sqlDir

    Project(Map properties) {
        name = properties['project.name']
        wsAppName = properties['ws.app.name']
        wsNodeName = properties['ws.node.name']
        wsFiles = properties['ws.files']
        sqlDir = properties['sql.dir']
    }

}
