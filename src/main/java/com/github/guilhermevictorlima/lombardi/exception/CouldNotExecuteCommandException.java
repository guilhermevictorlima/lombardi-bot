package com.github.guilhermevictorlima.lombardi.exception;

import com.github.guilhermevictorlima.lombardi.api.commands.BotCommand;

import static java.lang.String.format;

public class CouldNotExecuteCommandException extends RuntimeException {

    public <T extends BotCommand> CouldNotExecuteCommandException(Class<T> commandClass, String message) {
        super(format("Could not execute %s: %s", commandClass.getSimpleName(), message));
    }
}
