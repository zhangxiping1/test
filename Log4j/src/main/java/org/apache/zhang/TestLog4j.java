package org.apache.zhang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

public class TestLog4j {
    /**
     * 日志:打印=位置参考log4j.properties
     */
//    public static final Logger LOG = LoggerFactory.getLogger("zxp");
//    public static final Logger LOG = LoggerFactory.getLogger(TestLog4j.class);
    public static final Logger LOG = LoggerFactory.getLogger("zxp");

    public static void main(String[] args) throws IOException {

        HashMap ha = new HashMap();
        ha.put("a","b");
        ha.put("a",2);

        System.out.println(ha.keySet().toString());

        new TestLog4j().printTestLog();

    }

    public void printTestLog(){
        //debug级别的日志
        LOG.debug("this is a debug log");

        //info级别的日志
        LOG.info("this is a info log");

        //warn级别的日志
        LOG.warn("this is a warn log");


        //error级别的日志
        LOG.error("this is a error log",new RuntimeException());

        /**
         * 备注：
         * 看你当前项目在C、D、E还是F盘，然后查看对应盘符下的/data/logs/ssmTest/common/common.log 是否有写入日志信息<br/>
         * 查看对应盘符下的/data/logs/ssmTest/error/error.log 是否有写入错误日志信息
         */
    }

}