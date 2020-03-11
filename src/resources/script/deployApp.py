import sys
import os.path
import time
import application_util as app_util
from java.io import FileInputStream
from java.util import Properties

prop = Properties()
prop.load(FileInputStream(sys.argv[0] + '/config_app.properties'))

cellName = prop.get('ws.cell.name')
serverName = prop.get('ws.server.name')
nodeName = prop.get('ws.node.name')
cluster = prop.get('ws.cluster.name')
contextPath = prop.get('ws.context.path')
virtualHost = prop.get('ws.virtual.host')
appName = prop.get('ws.app.name')
warPath = prop.get('linux.war.path')
classloaderMode = prop.get('ws.classloader.mode')

print('serverName is %s' % (serverName))
print('nodeName is %s' % (nodeName))
print('cluster is %s' % (cluster))
print('contextPath is %s' % (contextPath))
print('virtualHost is %s' % (virtualHost))
print('appName is %s' % (appName))
print('warPath is %s' % (warPath))

def checkWarExist(warPath):
    if not os.path.isfile(warPath) :
        raise Exception('Update war file not found, path: ' + warPath)

def checkAppExist(appName):
    appList = AdminApplication.listApplications()
    return appName in appList

def isAppStop(cellName, appName, process):
    apps = app_util.findAllAppsInCell(cellName, 1)
    for app in apps:
        if app.find('WebSphere:name=' + appName) != -1 and app.find('process=' + process) != -1:
            print 'app %s/%s is running' % (process, appName)
            return 0
    print 'app %s/%s is stop' % (process, appName)
    return 1

def stopApp(appManager, appName, process):
    print 'Stop app: %s/%s...' % (process, appName)
    AdminControl.invoke(appManager, 'stopApplication', appName)
    print 'Stop app %s/%s success.' % (process, appName)

def updateApp(appName, warPath):
    print 'update app: %s...' % appName
    AdminApp.update(appName, 'app', ['-operation', 'update', '-contents', warPath])
    AdminConfig.save()
    print 'Update app %s success.' % (appName)

def installApp(cluster, appName, contextPath, virtualHost, warPath):
    options = []

    options.append('-cluster')
    options.append(cluster)

    options.append('-appname')
    options.append(appName)

    options.append('-contextroot')
    options.append(contextPath)

    options.append('-MapWebModToVH')
    options.append([['.*', '.*', virtualHost]])

    options.append('-usedefaultbindings')
    AdminApp.install(warPath, options)
    AdminConfig.save()
    app_util.syncClusterNodes(cluster)

def startApp(appManager, appName, process):
    print 'Start app: %s/%s...' % (process, appName)
    print 'AdminControl.invoke( %s, startApplication, %s)' % (appManager, appName)
    AdminControl.invoke(str(appManager), 'startApplication', appName)
    print 'Start app: %s/%s success.' % (process, appName)

def uninstallApp(appName):
    AdminApp.uninstall(appName)
    AdminConfig.save()
    print 'Uninstall app: %s success.' % (appName)

def waitExtractAppBinaryFile(appName):
    print 'wait system extracts all application binary files..'
    result = AdminApp.isAppReady(appName)
    count = 0
    while (result == "false"):
       time.sleep(5)
       result = AdminApp.isAppReady(appName)
       count = count + 1
       if count >= 60:
        raise Exception('wait system extracts all application binary files timeout, appName:%s' % appName)
    print("system extracts binary files success...")
    print AdminApp.getDeployStatus(appName)

def main():
    checkWarExist(warPath)
    checkAppExist(appName)

    if checkAppExist(appName):
        appInstances = app_util.findAppsByNameFromCell(cellName, appName)
        print 'appInstances: %s' % appInstances
        for appInstance in appInstances:
            attrMap = app_util.getAppAttributes(appInstance)
            process = attrMap['process']
            appManager = app_util.getAppManager(cellName, process)
            if appManager is None:
                print 'Application manager not found, cell:%s server:%s' %(cellName, process)
                continue
            isStop = isAppStop(cellName, appName, process)
            if isStop == 0:
                stopApp(appManager, appName, process)

        updateApp(appName, warPath)
        waitExtractAppBinaryFile(appName)

        if classloaderMode is not None:
            app_util.setClassLoaderMode(appName, classloaderMode)

        for appInstance in appInstances:
            attrMap = app_util.getAppAttributes(appInstance)
            process = attrMap['process']
            appManager = app_util.getAppManager(cellName, process)
            if appManager is None:
                print 'Application manager not found, cell:%s server:%s' %(cellName, process)
                continue
            startApp(appManager, appName, process)
    else:
        installApp(cluster, appName, contextPath, virtualHost, warPath)
        waitExtractAppBinaryFile(appName)
        if classloaderMode is not None:
            app_util.setClassLoaderMode(appName, classloaderMode)
        processes = app_util.getServersInCluster(cluster)
        print 'find processes in cluster%s: %s' % (cluster, processes)
        for process in processes:
            appManager = app_util.getAppManager(cellName, process)
            if appManager is None:
                print 'Application manager not found, cell:%s server:%s' %(cellName, process)
                continue
            startApp(appManager, appName, process)
    print 'app info:\n'
    print app_util.getAppInfo(appName)

main()
