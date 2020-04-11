package com.analysis.ws

import groovy.json.JsonBuilder
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WsFileParserTest {

    WsFileParser wsFileParser;

    @BeforeEach
    void setUp() {
        wsFileParser = new WsFileParser()
    }

    @Test
    void parse() {
        def wsFileText = '''
            |NAME       MY_PROJECT
            |DEF        NAME=Mark
            |DEF        FULL_NAME=${NAME}Huang
            |SSH_URL    ${FULL_NAME}@192.168.36.91
            |DEF        DEFAULT_OWN=mark:ws
            |SCP        /tmp/123/dir/* => /tmp/456/dir!${DEFAULT_OWN}:755
            |SSH_RUN    echo $${USER} ${FULL_NAME}
            |RUN        echo $${USER} ${FULL_NAME}
        '''.stripMargin('|')

        def ans = wsFileParser.parse(wsFileText)

        assertEquals(new JsonBuilder(ans).toString(), '{"tasks":[{"type":"SCP","content":"/tmp/123/dir/* => /tmp/456/dir!mark:ws:755"},{"type":"SSH_RUN","content":"echo ${USER} MarkHuang"},{"type":"RUN","content":"echo ${USER} MarkHuang"}],"sshUrl":{"user":"MarkHuang","port":"22","url":"192.168.36.91"},"name":"MY_PROJECT"}')
    }

    @Test
    void parseScp() {
        def scp = '/tmp/123/dir/* => /tmp/456/dir!mark:ws:755'
        def ans = WsFileParser.parseScp(scp)

        assertEquals(ans['target'], '/tmp/123/dir/*')
        assertEquals(ans['dest'], '/tmp/456/dir')
        assertEquals(ans['user'], 'mark')
        assertEquals(ans['owner'], 'ws')
        assertEquals(ans['permission'], '755')
    }

}
