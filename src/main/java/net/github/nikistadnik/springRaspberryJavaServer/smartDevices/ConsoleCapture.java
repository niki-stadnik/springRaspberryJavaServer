package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import org.json.JSONObject;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

@Component
public class ConsoleCapture implements InitializingBean, DisposableBean {
    private ByteArrayOutputStream outBuffer;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Create a ByteArrayOutputStream to capture the output
        outBuffer = new ByteArrayOutputStream();

        // Create a custom PrintStream that writes to the ByteArrayOutputStream
        PrintStream customOut = new PrintStream(outBuffer);

        // Save the original System.out and System.err
        originalOut = System.out;
        originalErr = System.err;

        // Create a TeeOutputStream that duplicates the output to both originalOut and customOut
        OutputStream teeOut = new TeeOutputStream(originalOut, customOut);
        OutputStream teeErr = new TeeOutputStream(originalErr, customOut);

        // Redirect System.out and System.err to the TeeOutputStreams
        System.setOut(new PrintStream(teeOut));
        System.setErr(new PrintStream(teeErr));
    }

    @Override
    public void destroy() throws Exception {
        // Restore the original System.out and System.err
        System.setOut(originalOut);
        System.setErr(originalErr);

        // Close the ByteArrayOutputStream
        outBuffer.close();
    }

    // Method to retrieve captured output
    public String getCapturedOutput() {
        return outBuffer.toString();
    }

    ///////////////////////////////////////////////

    //sending to web app
    private String lastSentOutput = "";
    static JSONObject jo;
    @Scheduled(fixedDelay = 1000) // Adjust the delay as needed
    public void sendCapturedOutputToWebSocket() {
        // Retrieve the captured output from the ByteArrayOutputStream
        String capturedOutput = outBuffer.toString();

        // Check if there is any new output since the last sent output
        if (!capturedOutput.equals(lastSentOutput)) {
            // Send captured output to WebSocket clients subscribed to "/topic/cons"
            SendMessage.sendMessage("/topic/console", capturedOutput);

            // Update the last sent output
            lastSentOutput = capturedOutput;
        }
    }
    ///////////////////////////////////////////////////////////



    // Define TeeOutputStream class
    private static class TeeOutputStream extends OutputStream {
        private final OutputStream out1;
        private final OutputStream out2;

        public TeeOutputStream(OutputStream out1, OutputStream out2) {
            this.out1 = out1;
            this.out2 = out2;
        }

        @Override
        public void write(int b) throws IOException {
            out1.write(b);
            out2.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            out1.write(b);
            out2.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out1.write(b, off, len);
            out2.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            out1.flush();
            out2.flush();
        }

        @Override
        public void close() throws IOException {
            out1.close();
            out2.close();
        }
    }
}