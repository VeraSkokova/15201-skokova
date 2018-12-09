package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;

public class ByteReader {
    private static final Logger logger = LogManager.getLogger(XMLClient.class);

    private DataInputStream inputStream;

    public ByteReader(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] readMessage() throws IOException {
        byte[] message = null;
        int length = inputStream.readInt();

        if (length < 0) {
            logger.warn("Can't read");
        } else {
            int read = 0;
            message = new byte[length];
            while (read < length) {
                int temp = inputStream.read(message, read, length - read);
                read += temp;
            }
        }
        return message;
    }
}
