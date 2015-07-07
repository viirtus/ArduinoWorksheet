package ru.gubkin.lk.arduinoworksheet.component.terminal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

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
    private final Context context;
    private final ConnectionHandler connectionHandler;
    private Terminal terminal;
    private TerminalAdapter adapter;

    private Button newCommandButton;

    public TerminalController(Context context, ConnectionHandler connectionHandler, int deviceId) {
        super(context, TITLE, deviceId);
        this.context = context;
        this.connectionHandler = connectionHandler;
        terminal = new Terminal();
        adapter = new TerminalAdapter(context, this);
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
        if (newCommandButton != null) {
            newCommandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = getPromptDialog();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public void unregisterListeners() {

    }

    @Override
    public void notifyChange() {
        validateView(300, 1, 1);

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
        String message = builder.addDate().addMessage(">>: " + m).getMessage();
        terminal.postMessage(message);
    }

    @Override
    public void onMessageReceive(String m) {
        MessageBuilder builder = new MessageBuilder();
        String message = builder.addDate().addMessage("<<: " + m).getMessage();
        terminal.postMessage(message);
    }

    public void setNewCommandButton(Button newCommandButton) {
        this.newCommandButton = newCommandButton;
    }

    public void setView(TextView view) {
        terminal.setView(view);
    }

    class MessageBuilder {
        private String dateFormat = "HH:mm:ss";
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

    @NonNull
    private Dialog getPromptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Новая команда");

        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                connectionHandler.sendData(input.getText().toString());
                onMessageReceive(input.getText().toString());
                onOutputMessage(input.getText().toString());
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
