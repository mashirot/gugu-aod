package ski.mashiro.config

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import ski.mashiro.common.GlobalBean
import ski.mashiro.entity.config.SystemConfig
import ski.mashiro.file.ConfigFileOperation
import kotlin.text.Charsets.UTF_8

/**
 * @author mashirot
 * 2024/1/8 18:29
 */
object LoggerConfig {
    fun initLogger() {
        if (!ConfigFileOperation.systemConfigFile.exists()) {
            GlobalBean.systemConfig = SystemConfig()
        } else {
            loadSystemConfig()
        }

        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.reset()

        val logFolder = "${GlobalBean.RESOURCES_FOLDER.path}/logs"

        // Create an encoder
        val encoder = PatternLayoutEncoder()
        encoder.context = loggerContext
        encoder.pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{56}.%method:%L - %msg%n"
        encoder.charset = UTF_8
        encoder.start()

        val consoleAppender = ConsoleAppender<ILoggingEvent>()
        consoleAppender.context = loggerContext
        consoleAppender.name = "console"
        consoleAppender.encoder = encoder
        consoleAppender.start()

        // Create a file appender
        val fileAppender = FileAppender<ILoggingEvent>()
        fileAppender.context = loggerContext
        fileAppender.name = "file"
        val fileNamePattern = "$logFolder/log.log"
        fileAppender.file = fileNamePattern
        fileAppender.encoder = encoder
        fileAppender.start()

        // Get the root logger and add appenders
        val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        rootLogger.level = Level.toLevel(GlobalBean.systemConfig.logLevel)
        rootLogger.addAppender(consoleAppender)
        rootLogger.addAppender(fileAppender)
    }

    private fun loadSystemConfig() {
        val systemConfigYaml = FileUtils.readFileToString(ConfigFileOperation.systemConfigFile, UTF_8)
        GlobalBean.systemConfig = GlobalBean.YAML_MAPPER.readValue(systemConfigYaml, SystemConfig::class.java)
    }

}