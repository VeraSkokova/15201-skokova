package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;

public class ByteReader {
    private static final Logger logger = LogManager.getLogger(ByteReader.class);

    private DataInputStream inputStream;

    public ByteReader(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] readMessage() {
        byte[] message = null;
        try {
            int length = inputStream.readInt();
            int read = 0;
            message = new byte[length];
            while (read < length) {
                int temp = inputStream.read(message, read, length - read);
                read += temp;
            }
        } catch (IOException e) {
            logger.error("Error in reading message: " + e.getMessage());
        }
        return message;
    }
}
