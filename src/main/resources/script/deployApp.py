import sys
import os.path
import time
import application_util as app_util
from java.io import FileInputStream
from java.util import Properties

prop = Properties()
confPath = sys.argv[0] + '/config_app.properties'
prop.load(FileInputStream(confPath))

warPath = sys.argv[1]
cellName = prop.get('ws.cell.name')
cluster = prop.get('ws.cluster.name')
contextPath = prop.get('ws.context.path')
virtualHost = prop.get('ws.virtual.host')
appName = prop.get('ws.app.name')
classloaderMode = prop.get('ws.classloader.mode')
classloaderPolicy = prop.get('ws.classloader.policy')
sharedLib = prop.get('ws.shared.lib')
filePermission = prop.get('ws.adv.filePermission')
jspReloadTime = prop.get('ws.adv.jspReloadTime')
reloadInterval = prop.get('ws.adv.appReloadInterval')

print('configure:')
print('-' * 75)
print open(confPath, 'r').read()

appConfigOption = {
    'contextPath': contextPath,
    'virtualHost': virtualHost,
    'filePermission': filePermission,
    'jspReloadTime': jspReloadTime,
    'reloadInterval': reloadInterval,
    'sharedLib': sharedLib
}

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

def updateApp(appName, warPath, appConfigOption):
    print 'update app: %s...' % appName
    options = []
    options.append('-operation')
    options.append('update')

    options.append('-contents')
    options.append(warPath)

    setAppConfigOption(appConfigOption, options)
    AdminApp.update(appName, 'app', options)
    AdminConfig.save()
    print 'Update app %s success.' % (appName)

def installApp(cluster, appName, contextPath, appConfigOption):
    options = []

    options.append('-cluster')
    options.append(cluster)

    options.append('-appname')
    options.append(appName)

    setAppConfigOption(appConfigOption, options)
    AdminApp.install(warPath, options)
    AdminConfig.save()
    app_util.syncClusterNodes(cluster)

def setAppConfigOption(appConfigOption, options = []):
        contextPath = appConfigOption.get('contextPath')
        virtualHost = appConfigOption.get('virtualHost')
        filePermission = appConfigOption.get('filePermission')
        jspReloadTime = appConfigOption.get('jspReloadTime')
        reloadInterval = appConfigOption.get('reloadInterval')
        sharedLib = appConfigOption.get('sharedLib')

        options.append('-CtxRootForWebMod')
        options.append([['.*', '.*', '/' + contextPath]])

        options.append('-MapWebModToVH')
        options.append([['.*', '.*', virtualHost]])

        # Advance setting
        if filePermission is not None:
            options.append('-filepermission')
            options.append(filePermission)

        if jspReloadTime is not None:
            options.append('-JSPReloadForWebMod')
            options.append([['.*', '.*', 'Yes', jspReloadTime]])

        if reloadInterval is not None:
            options.append('-reloadEnabled')
            options.append('-reloadInterval')
            options.append(reloadInterval)

        if sharedLib is not None:
            libNames = ''
            for libName in sharedLib.split('|'):
                libNames = libNames + libName + '+'
            options.append('-MapSharedLibForMod')
            options.append([[ appName, '.*', libNames[:-1] ]])

        options.append('-usedefaultbindings')

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

def setWarConfigure(appName, classloaderMode, classloaderPolicy, sharedLib):
    if classloaderMode is not None:
        app_util.setClassLoaderMode(appName, classloaderMode)

    if classloaderPolicy is not None:
        app_util.setClassLoaderPolicy(appName, classloaderPolicy)

def setAdvanceAppModuleConfig(appName, prop):
    from java.util import HashMap

    m = HashMap(prop)
    configMap = {}
    for key in m.keySet():
        if(key.startswith('ws.adv.prop')):
            configMap[str(key[12:])] = str(m[key])
    if len(configMap.keys()) != 0:
        app_util.setAdvanceModuleConfig(appName, configMap)

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

        updateApp(appName, warPath, appConfigOption)
        waitExtractAppBinaryFile(appName)
        setWarConfigure(appName, classloaderMode, classloaderPolicy, sharedLib)
        setAdvanceAppModuleConfig(appName, prop)

        for appInstance in appInstances:
            attrMap = app_util.getAppAttributes(appInstance)
            process = attrMap['process']
            appManager = app_util.getAppManager(cellName, process)
            if appManager is None:
                print 'Application manager not found, cell:%s server:%s' %(cellName, process)
                continue
            startApp(appManager, appName, process)
    else:
        installApp(cluster, appName, contextPath, appConfigOption)
        waitExtractAppBinaryFile(appName)
        setWarConfigure(appName, classloaderMode, classloaderPolicy, sharedLib)
        setAdvanceAppModuleConfig(appName, prop)

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
