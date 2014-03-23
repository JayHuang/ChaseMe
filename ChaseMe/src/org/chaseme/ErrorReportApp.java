package org.chaseme;

import org.chaseme.file.IO.ExceptionWriter;

import android.app.Application;

/**
 * Creates new exception handler to save stacktrace to SD card
 */
public class ErrorReportApp extends Application {
	private Thread.UncaughtExceptionHandler exceptionHandler;

	private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			new ExceptionWriter(ex).saveStackTraceToSD();
			exceptionHandler.uncaughtException(thread, ex);
		}
	};

	/**
	 * Sets default uncaught exception handler
	 */
	public void onCreate() {
		super.onCreate();
		exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}

}
