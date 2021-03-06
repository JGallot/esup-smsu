package org.esupportail.smsu.services.scheduler;

import org.apache.log4j.Logger;

/**
 * Exception handler for quartz error.
 * @author PRQD8824
 *
 */
public class QuartzExceptionHandler {

	/**
     * logger.
     */
	private final Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 
	 * @param taskTitle
	 * @param exception
	 */
    public void process(final String taskTitle, final Throwable exception) {

        logger.error("A DataAccessException occurred during the task [" + taskTitle + "] : " +
                 exception.getClass(), exception);

    }
}
