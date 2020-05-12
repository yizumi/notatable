package com.apcandsons.notation;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Notator {
    static Staff staff = new Staff();

    public static void main(String[] args) throws LineUnavailableException, IOException {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100, 1024, 1);

        PitchDetectionHandler handler = new PitchDetectionHandler() {
            int maxLen = 2;
            Pitch lastPitch = null;
            ArrayList<Pitch> list = new ArrayList<>();
            Instrument instrument = new Cello();

            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent audioEvent) {
                float f = result.getPitch();
                Pitch p = Pitch.fromFrequency(f);
                if (p == null) {
                    return;
                }
                if (!instrument.isPitchInRange(p)) {
                    return;
                }
                while(list.size() > maxLen) {
                    list.remove(0);
                }
                list.add(p);
                for (int i = 0; i < list.size() - 1; i++) {
                    if (list.get(i) != list.get(i+1)) {
                        return;
                    }
                }
                if (lastPitch != list.get(0)) {
                    lastPitch = list.get(0);
                    Note note = new Note(list.get(0), f);
                    System.out.println(note);
                    staff.addNote(note);
                }
            }
        };

        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 1024, handler);
        dispatcher.addAudioProcessor(pitchProcessor);

        startWebServer();

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    public static void startWebServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(10101), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = staff.toJSON();
            t.getResponseHeaders().add("Content-Type", "application/json");
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
