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
    String linuxWarPath
    String linuxDPath
    String wsAppName
    String wsNodeName
    String wsFiles

    Project(Map properties) {
        name = properties['project.name']
        linuxWarPath = properties['linux.war.path']
        linuxDPath = properties['linux.d.path']
        wsAppName = properties['ws.app.name']
        wsNodeName = properties['ws.node.name']
        wsFiles = properties['ws.files']
    }

}
