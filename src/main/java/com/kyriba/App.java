package com.kyriba;

import com.kyriba.configurations.Configurator;
import com.kyriba.configurations.ConsoleConfigurator;
import com.kyriba.core.DirectoryHandler;
import com.kyriba.core.DirectoryHandlerFactory;
import com.kyriba.core.PooledDirectoryHandlerFactory;

public class App {
    public static void main(String[] args) {
        System.out.println("Simple tool for logs analysis has been launched.");

        final Configurator configurator = new ConsoleConfigurator();
        final DirectoryHandlerFactory factory = new PooledDirectoryHandlerFactory(configurator);
        final DirectoryHandler handler = factory.newDirectoryHandler();

        try {
            handler.handle();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Program terminated");
            System.exit(1);
        }

        System.out.println("Simple tool for logs analysis has been completed.");
    }
}
