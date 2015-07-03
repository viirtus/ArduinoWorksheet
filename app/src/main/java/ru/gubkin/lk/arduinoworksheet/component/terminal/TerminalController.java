package ru.gubkin.lk.arduinoworksheet.component.terminal;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.gubkin.lk.arduinoworksheet.MainActivity;
import ru.gubkin.lk.arduinoworksheet.adapter.TerminalAdapter;
import ru.gubkin.lk.arduinoworksheet.component.Controller;
import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.connect.OutputListener;
import ru.gubkin.lk.arduinoworksheet.connect.ReceiverListener;

/**
 * Created by root on 29.06.15.
 */
public class TerminalController extends Controller<Terminal> implements ReceiverListener, OutputListener {
    private static final String TITLE = "Терминал соединения";
    private Terminal terminal;
    private TerminalAdapter adapter;

    public TerminalController(Context context, ConnectionHandler connectionHandler, int deviceId) {
        super(context, TITLE, deviceId);
        terminal = new Terminal();
        adapter = new TerminalAdapter(context, terminal);
        if (!MainActivity.debug) {
            connectionHandler.setReceiverListener(this);
            connectionHandler.setOutputListener(this);
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void unregisterListeners() {

    }

    @Override
    public void notifyChange() {
        validateView(250, 1, 1);

    }

    @Override
    public void updateComponent(Terminal o) {

    }

    @Override
    public void deleteComponent(Terminal o) {

    }

    @Override
    public void processComponent(Terminal o) {

    }

    @Override
    public void setGridView(GridView gridView) {
        super.setGridView(gridView);
        gridView.setNumColumns(1);
    }

    @Override
    public void onOutputMessage(String m) {
        MessageBuilder builder = new MessageBuilder();
        String message = builder.addDate().addMessage("отправлено: " + m).getMessage();
        terminal.postMessage(message);
    }

    @Override
    public void onMessageReceive(String m) {
        MessageBuilder builder = new MessageBuilder();
        String message = builder.addDate().addMessage("принято: " + m).getMessage();
        terminal.postMessage(message);
    }

    class MessageBuilder {
        private String dateFormat = "dd.MM.yyyy HH:mm:ss";
        private String current = "";

        public MessageBuilder addDate() {
            SimpleDateFormat formater = new SimpleDateFormat(dateFormat, Locale.US);
            current += "[" + formater.format(new Date()) + "] ";
            return this;
        }

        public MessageBuilder addMessage(String message) {
            current += message;
            return this;
        }

        public String getMessage() {
            return current;
        }
    }
}
