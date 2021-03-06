/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.taps.aa.log;

import org.drools.spi.KnowledgeHelper;
import org.slf4j.Logger;

/**
 *
 * @author esteban
 */
public class RulesLoggerHelper {
    
    private static String formatMessage(KnowledgeHelper helper, String originalMessageFormat){
        return "\t[Rule: '"+helper.getRule().getName()+"'] ->   "+originalMessageFormat;
    }  
    
    public static void debug(Logger logger, KnowledgeHelper helper, String messageFormat, Object... args){
        logger.debug(formatMessage(helper, messageFormat), args);
    }
    
    public static void error(Logger logger, KnowledgeHelper helper, String messageFormat, Object... args){
        logger.error(formatMessage(helper, messageFormat), args);
    }
    
    public static void info(Logger logger, KnowledgeHelper helper, String messageFormat, Object... args){
        logger.info(formatMessage(helper, messageFormat), args);
    }
    
    public static void trace(Logger logger, KnowledgeHelper helper, String messageFormat, Object... args){
        logger.trace(formatMessage(helper, messageFormat), args);
    }
    
    public static void warn(Logger logger, KnowledgeHelper helper, String messageFormat, Object... args){
        logger.warn(formatMessage(helper, messageFormat), args);
    }
    
}
