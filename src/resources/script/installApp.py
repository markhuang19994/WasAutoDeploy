import sys
import os.path

serverName = sys.argv[0]
appName = sys.argv[1]
nodeName = sys.argv[2]
contextPath = sys.argv[3]
warPath = sys.argv[4]
cluster = sys.argv[5]

print('ServerName is %s' % (serverName))
print('AppName is %s' % (appName))
print('NodeName is %s' % (nodeName))
print('Cluster is %s' % (cluster))
print('ContextPath is %s' % (contextPath))
print('WarPath is %s' % (warPath))

def checkWarExist(warPath):
	if not os.path.isfile(warPath) :
		raise Exception('Update war file not found, path: ' + warPath)	

def checkAppExist(appName):
	appList = AdminApplication.listApplications()
	if appName in appList:
		raise Exception('App: %s is installed' % (appName))

def installApp(appName, warPath, nodeName, serverName, contextPath):
    options = []

    options.append('-server')
    options.append(serverName)

    options.append('-node')
    options.append(nodeName)

    options.append('-contextroot')
    options.append(contextPath)

    options.append('-cluster')
    options.append(cluster)

    options.append('-appname')
    options.append(appName)

    options.append('-MapWebModToVH')
    options.append([['.*', '.*', 'cola_default_host']])

    options.append('-usedefaultbindings')
    AdminApp.install(warPath, options)
    AdminConfig.save()

def startApp(appName, nodeName):
	AdminControl.invoke(AdminControl.queryNames('type=ApplicationManager,node=' + nodeName + ',*'), 'startApplication', appName)
	print('Start app: %s success' % (appName))

def main():
    checkWarExist(warPath)
    checkAppExist(appName)

    installApp(appName, warPath, nodeName, serverName, contextPath)
    startApp(appName, nodeName)

main()
	



