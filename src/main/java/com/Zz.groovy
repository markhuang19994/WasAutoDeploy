package com

/**
 * @since 6/3/20
 * @author Mark Huang
 */
class Zz {
    static String s = '''
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/filter/MomLogContextFilter.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/filter/PostFormDataFilter.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/filter/PostFormDataWrapper.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/filter/StepValidatorFilter.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/CapHandlerServlet.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/MOMBaseHandler.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/MOMReStepHandler.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/MOMRouterHandler.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/MOMStep1Handler.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/MOMStep2Handler.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/handler/MOMStep3Handler.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/rpt/service/impl/HandOffExcelRptServiceImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/service/BrowserLogService.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/service/impl/BrowserLogServiceImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/service/impl/MomConfirmPageServiceImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/service/impl/MomPackServiceImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/app/sitemesh/MomRequestJSONMapper.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/ApplicationContextProvider.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/PageDataProvider.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/Project.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/UnitKeyGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/cache/Cache.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/AutoRefreshScript.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/mypagesave/provider/helper/PageDataProviderHelper.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/SimpleTypeConverter.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/generator/PersistGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/generator/PrettierPersistGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/generator/impl/DefaultPersistJsonTypeGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/generator/impl/JsonPersistGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/generator/impl/PersistObjectGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/generator/impl/YamlPersistGenerator.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/model/GenerateResult.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/model/JsonField.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/model/PersistReadJson.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/model/ReadProcessorResult.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/generate/KeyPersistGeneratorProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/generate/PersistGeneratorProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/generate/ValuePersistGeneratorProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/generate/impl/DefaultPersistGeneratorProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/generate/impl/KeyPersistGeneratorProcessorImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/generate/impl/ValuePersistGeneratorProcessorImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/JsonTypeKeyPersistReadProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/JsonTypePersistReadProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/JsonTypeValuePersistReadProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/ObjectPersistReadProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/ReadProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/impl/DefaultJsonTypePersistReadProcessor.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/impl/JsonTypeKeyPersistReadProcessorImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/impl/JsonTypeValuePersistReadProcessorImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/processor/read/impl/ObjectPersistReadProcessorImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/reader/PersistReader.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/reader/impl/DefaultJsonTypePersistReader.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/reader/impl/JsonPersistReader.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/reader/impl/ObjectPersistReader.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/reader/impl/YamlPersistReader.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/util/ObjectPersistUtil.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/util/PersistJson.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/util/ReflectionUtil.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/dev/persist/util/ThreadUtil.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/log/MomLogPatternLayout.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/log/MomLogger.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/log/MomLoggerFactory.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/log/MomLoggerImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/web/CCHttpSession.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/web/CCHttpSessionImpl.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/tool/web/CCWebUtils.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/web/ArMap.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/web/MomHandlerServlet.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/web/MomSessionAttributeListener.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/web/MomSessionLifeListener.java
impl/workspace/mom/mom-app/src/main/java/com/iisigroup/mom/web/ProgramTracer.java
impl/workspace/mom/mom-common/src/main/java/com/iisigroup/colabase/common/constants/MOMConstants.java
impl/workspace/mom/mom-common/src/main/java/com/iisigroup/colabase/common/log/MOMStepLogAppender.java
impl/workspace/mom/mom-common/src/main/java/com/iisigroup/colabase/common/log/MOMTimeFolderSizeRollingFileAppender.java
impl/workspace/mom/mom-config/src/main/resources/batch/quartz.xml
impl/workspace/mom/mom-config/src/main/resources/config.properties
impl/workspace/mom/mom-config/src/main/resources/db/database.properties
impl/workspace/mom/mom-config/src/main/resources/db/hibernate.properties
impl/workspace/mom/mom-config/src/main/resources/db/jpa.xml
impl/workspace/mom/mom-config/src/main/resources/ftl/ApplicationReport/DBU.ftl
impl/workspace/mom/mom-config/src/main/resources/log4j.properties
impl/workspace/mom/mom-config/src/main/resources/project_info.txt
impl/workspace/mom/mom-config/src/main/resources/spring/component.xml
impl/workspace/mom/mom-web/.babelrc
impl/workspace/mom/mom-web/package-lock.json
impl/workspace/mom/mom-web/package.json
impl/workspace/mom/mom-web/pom.xml
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/sitemesh.xml
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/sitemesh/blank.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/sitemesh/formTemplate.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/sitemesh/none.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/viewpage/step1.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/viewpage/step2.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/viewpage/step3.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/viewpage/step4.jsp
impl/workspace/mom/mom-web/src/main/webapp/WEB-INF/web.xml
impl/workspace/mom/mom-web/src/main/webapp/static/PDF/DBU/css/styles3.css
impl/workspace/mom/mom-web/src/main/webapp/static/build.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/common/cust.common.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/dev/autoCompletePage.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/es5/browserLog.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/es5/browserLog.js.map
impl/workspace/mom/mom-web/src/main/webapp/static/js/es6/browserLog.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/step1.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/step2.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/step3.js
impl/workspace/mom/mom-web/src/main/webapp/static/js/step4.js
impl/workspace/mom/mom-web/src/main/webapp/static/lib/js/capjs.js
impl/workspace/mom/mom-web/src/main/webapp/static/lib/js/common/common.js
impl/workspace/mom/mom-web/src/main/webapp/static/lib/js/common/common.properties.js
impl/workspace/mom/mom-web/src/main/webapp/static/lib/js/libjs.js
impl/workspace/mom/mom-web/src/main/webapp/static/lib/js/sow/select2.js
impl/workspace/mom/mom-web/src/main/webapp/static/main.js
impl/workspace/mom/mom-web/src/main/webapp/static/main.min.js
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-base/4.0.1-colabase-jdk1.7-202005281700/citi-base-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-base/4.0.1-colabase-jdk1.7-202005281700/citi-base-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-captcha/4.0.1-colabase-jdk1.7-202005281700/citi-captcha-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-captcha/4.0.1-colabase-jdk1.7-202005281700/citi-captcha-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-core/4.0.1-colabase-jdk1.7-202005281700/citi-core-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-core/4.0.1-colabase-jdk1.7-202005281700/citi-core-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-db/4.0.1-colabase-jdk1.7-202005281700/citi-db-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-db/4.0.1-colabase-jdk1.7-202005281700/citi-db-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-edm/4.0.1-colabase-jdk1.7-202005281700/citi-edm-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-edm/4.0.1-colabase-jdk1.7-202005281700/citi-edm-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-image/4.0.1-colabase-jdk1.7-202005281700/citi-image-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-image/4.0.1-colabase-jdk1.7-202005281700/citi-image-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-import/4.0.1-colabase-jdk1.7-202005281700/citi-import-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-import/4.0.1-colabase-jdk1.7-202005281700/citi-import-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-json/4.0.1-colabase-jdk1.7-202005281700/citi-json-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-json/4.0.1-colabase-jdk1.7-202005281700/citi-json-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-moica/4.0.1-colabase-jdk1.7-202005281700/citi-moica-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-moica/4.0.1-colabase-jdk1.7-202005281700/citi-moica-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-mvc/4.0.1-colabase-jdk1.7-202005281700/citi-mvc-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-mvc/4.0.1-colabase-jdk1.7-202005281700/citi-mvc-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-net/4.0.1-colabase-jdk1.7-202005281700/citi-net-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-net/4.0.1-colabase-jdk1.7-202005281700/citi-net-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-otp/4.0.1-colabase-jdk1.7-202005281700/citi-otp-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-otp/4.0.1-colabase-jdk1.7-202005281700/citi-otp-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-pdf/4.0.1-colabase-jdk1.7-202005281700/citi-pdf-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-pdf/4.0.1-colabase-jdk1.7-202005281700/citi-pdf-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-report/4.0.1-colabase-jdk1.7-202005281700/citi-report-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-report/4.0.1-colabase-jdk1.7-202005281700/citi-report-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-security/4.0.1-colabase-jdk1.7-202005281700/citi-security-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-security/4.0.1-colabase-jdk1.7-202005281700/citi-security-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-ssl/4.0.1-colabase-jdk1.7-202005281700/citi-ssl-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-ssl/4.0.1-colabase-jdk1.7-202005281700/citi-ssl-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-zip/4.0.1-colabase-jdk1.7-202005281700/citi-zip-4.0.1-colabase-jdk1.7-202005281700.jar
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi-zip/4.0.1-colabase-jdk1.7-202005281700/citi-zip-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/mvn/repo/com/iisigroup/colabase/citi/4.0.1-colabase-jdk1.7-202005281700/citi-4.0.1-colabase-jdk1.7-202005281700.pom
impl/workspace/mom/pom.xml
impl/workspace/mom/src/Deploy/Task.groovy
impl/workspace/mom/src/Deploy/deploy_config.yml
impl/workspace/mom/src/Deploy/was/config_app.properties
impl/workspace/mom/src/Deploy/was/ws/server1.ws
impl/workspace/mom/src/Deploy/was/ws/server2.ws
impl/workspace/mom/src/DeploySQL/DeployUAT20191003.sql
impl/workspace/mom/src/script/groovy/resources/config.properties
impl/workspace/mom/src/script/groovy/resources/r.js
impl/workspace/mom/src/script/groovy/resources/workTreeIgnore.txt
impl/workspace/mom/src/script/groovy/src/GenerateMainMinJs.groovy
impl/workspace/mom/src/script/groovy/src/GenerateProjectInfo.groovy
impl/workspace/mom/src/script/groovy/src/ProfilePackage.groovy
impl/workspace/mom/src/script/groovy/src/common/CustomProperties.groovy
impl/workspace/mom/src/script/groovy/src/skipWorkingTree.groovy
    '''

    static void main(String[] args) {
//        def dir = new File('/home/mark/IdeaProjects/source/citi/CITI_MOM2')
//        def hash1 = '3a043441'
//        String res = s.split('\n').findAll {
//            if( it == '') return false;
//            def text = ("git diff ${hash1} HEAD -- " + it).execute([], dir).text
//            return text.contains('old mode') && text.contains('new mode')
//        }.join('\n')
//
//        res.split('\n').each {
//            new File("${dir.absolutePath}/${it}").delete()
//        }
        //git status --porcelain | grep -E '^A.*' | awk "{print \"$(pwd)/\"\$2}" | xargs chmod 644
    }


}
