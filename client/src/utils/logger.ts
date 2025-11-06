type LogLevel = 'error' | 'warn' | 'info' | 'debug';

interface LogContext {
  [key: string]: unknown;
}

class Logger {
  private isDev = import.meta.env.DEV;

  private log(level: LogLevel, message: string, context?: LogContext): void {
    if (!this.isDev && level === 'debug') {
      return;
    }

    const timestamp = new Date().toISOString();
    const logMessage = `[${timestamp}] [${level.toUpperCase()}] ${message}`;

    switch (level) {
      case 'error':
        console.error(logMessage, context || '');
        break;
      case 'warn':
        console.warn(logMessage, context || '');
        break;
      case 'info':
        if (this.isDev) {
          console.warn(logMessage, context || '');
        }
        break;
      case 'debug':
        if (this.isDev) {
          console.warn(logMessage, context || '');
        }
        break;
    }
  }

  error(message: string, context?: LogContext): void {
    this.log('error', message, context);
  }

  warn(message: string, context?: LogContext): void {
    this.log('warn', message, context);
  }

  info(message: string, context?: LogContext): void {
    this.log('info', message, context);
  }

  debug(message: string, context?: LogContext): void {
    this.log('debug', message, context);
  }
}

export const logger = new Logger();
