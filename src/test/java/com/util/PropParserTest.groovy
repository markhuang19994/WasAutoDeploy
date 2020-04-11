package com.util

import com.analysis.PropParser
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*;

class PropParserTest {
    PropParser propParser

    @BeforeEach
    void setUp() {
        propParser = new PropParser()
    }

    @Test
    void updateVarMap() {
        def key = 'name'
        def val = 'Mark'

        propParser.updateVarMap(key, val)
        assertEquals(propParser.getVarMap().get(key), "\"${val}\"".toString())
    }

    @Test
    void parseString() {
        def testStr = 'Hello my name is ${name}'

        propParser.updateVarMap('name', 'Mark')
        def ans = propParser.parseString(testStr)
        assertEquals('Hello my name is Mark', ans)
    }

}
