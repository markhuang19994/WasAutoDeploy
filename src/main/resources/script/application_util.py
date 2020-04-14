import sys
import java.lang.System  as sys
import re

lineSeparator = sys.getProperty('line.separator')

def findAllAppsInCell(cellName, excludeStopApp):
    queryType = 'J2EEApplication'
    if excludeStopApp == 1:
       queryType = 'Application'
    return AdminControl.queryNames('cell=%s,type=%s,*' % (cellName, queryType)).split(lineSeparator)

def findAllAppManagersInCell(cellName):
    return AdminControl.queryNames('cell=%s,type=ApplicationManager,*' % cellName).split(lineSeparator)

def findAppsByNameFromCell(cellName, appName):
    apps = findAllAppsInCell(cellName, 0)
    matchedApps = []
    for app in apps:
        ab = re.compile("^WebSphere:name=%s[\s\S]*$" % appName)
        if ab.match(app):
            matchedApps.append(app)
    return matchedApps

def getAppManager(cellName, process):
    appManagers = findAllAppManagersInCell(cellName)
    for appManager in appManagers:
       if appManager.find('process=%s' % process) > 0:
        return appManager

def getAppAttributes(app):
    attrs = app.replace('WebSphere:', '').split(',')
    appAttrMap = {}
    for attr in attrs:
        firstEq = attr.find('=')
        if firstEq > 0:
            appAttrMap[attr[:firstEq]] = attr[firstEq+1:]
    return appAttrMap

def getServerIdsInCluster(clusterName):
    cluster_conf_id = AdminConfig.getid("/ServerCluster:" + clusterName)
    if not cluster_conf_id:
        raise "Cluster %s does not exist!" % cluster_name

    server_conf_ids = AdminConfig.showAttribute(cluster_conf_id, "members")
    server_conf_ids = server_conf_ids[1:-1]
    return server_conf_ids

def getServerNodesInCluster(clusterName):
    server_names = []
    node_names = []
    server_conf_ids = getServerIdsInCluster(clusterName)
    for server_conf_id in server_conf_ids.split():
        server_names.append(AdminConfig.showAttribute(server_conf_id, "memberName"))
        node_names.append(AdminConfig.showAttribute(server_conf_id, "nodeName"))
    return [server_names, node_names]

def getServersInCluster(clusterName):
    return getServerNodesInCluster(clusterName)[0]

def getNodesInCluster(clusterName):
    return getServerNodesInCluster(clusterName)[1]

def syncClusterNodes(clusterName):
    nodeNames = getNodesInCluster(clusterName)
    for nodeName in nodeNames:
        sync = AdminControl.completeObjectName('type=NodeSync,node=%s,*' % (nodeName))
        AdminControl.invoke(sync, 'sync')
        print 'sync %s success.' % (nodeName)

def getDeployObject(appName):
    dep = AdminConfig.getid('/Deployment:%s/' % appName)
    return AdminConfig.showAttribute(dep, 'deployedObject')

# mode: PARENT_FIRST, PARENT_LAST
def setClassLoaderMode(appName, mode):
    depObject = getDeployObject(appName)
    classldr = AdminConfig.showAttribute(depObject, 'classloader')
    AdminConfig.modify(classldr, [['mode', mode]])
    AdminConfig.save()
    print 'change classloader mode success.'
    print AdminConfig.showall(classldr)

# policy: SINGLE, MULTIPLE
def setClassLoaderPolicy(appName, policy):
    depObject = getDeployObject(appName)
    AdminConfig.modify(depObject, [['warClassLoaderPolicy', policy]])
    AdminConfig.save()
    print 'change classloader policy success.'
    AdminConfig.show(depObject, 'warClassLoaderPolicy')

def setSharedLibrary(appName, libName):
    libs = AdminConfig.list('Library').split(lineSeparator)
    isLibExist = 0
    for lib in libs:
        if lib.startswith(libName):
            isLibExist = 1
            break
    if isLibExist == 0:
        print 'Shared lib:%s not found.' % libName
        return

    depObject = getDeployObject(appName)
    classLoad1 = AdminConfig.showAttribute(depObject, 'classloader')
    print AdminConfig.create('LibraryRef', classLoad1, [['libraryName', libName]])
    AdminConfig.save()

def getAppInfo(appName):
    return AdminApp.view(appName)

