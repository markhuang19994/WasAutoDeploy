import sys
import os.path

appName = sys.argv[0]
nodeName = sys.argv[1]
warPath = sys.argv[2]

print('AppName is %s' % (appName))
print('NodeName is %s' % (nodeName))
print('warPath is %s' % (warPath))

def checkWarExist(warPath):
	if not os.path.isfile(warPath) :
		raise Exception('Update war file not found, path: ' + warPath)	

def checkAppExist(appName):
	appList = AdminApplication.listApplications()
	if not appName in appList:
		raise Exception('App: %s is not install' % (appName))	

def isAppRunning(appName):
    objectName = AdminControl.completeObjectName('type=Application,name=' + appName + ',*')
    return objectName != ""

def stopApp(appName, nodeName):
	if isAppRunning(appName):
		AdminControl.invoke(AdminControl.queryNames('type=ApplicationManager,node='+ nodeName +',*'), 'stopApplication', appName)
		print('Stop app: %s success' % (appName))

def updateApp(appName, warPath):
	AdminApp.update(appName, 'app', ['-operation', 'update', '-contents', warPath])
	AdminConfig.save()
	print('Update app: %s success' % (appName))

def startApp(appName, nodeName):
	AdminControl.invoke(AdminControl.queryNames('type=ApplicationManager,node=' + nodeName + ',*'), 'startApplication', appName)
	print('Start app: %s success' % (appName))

def main():
    checkWarExist(warPath)
    checkAppExist(appName)

    stopApp(appName, nodeName)
    updateApp(appName, warPath)
    startApp(appName, nodeName)

main()
	



