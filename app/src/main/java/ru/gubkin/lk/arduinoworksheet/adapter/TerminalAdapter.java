package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.terminal.Terminal;
import ru.gubkin.lk.arduinoworksheet.component.terminal.TerminalController;

/**
 * Created by root on 29.06.15.
 */
public class TerminalAdapter extends BaseAdapter {
    private final TerminalController terminal;
    private LayoutInflater inflater;
    private View inflated;
    public TerminalAdapter(Context context, TerminalController terminal) {
        this.terminal = terminal;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflated == null) {
            inflated = inflater.inflate(R.layout.terminal_item, parent, false);
            TextView textView = (TextView) inflated.findViewById(R.id.text_terminal);
            Button newCommand = (Button) inflated.findViewById(R.id.add_command_btn);
            terminal.setView(textView);
            terminal.setNewCommandButton(newCommand);
            terminal.registerListeners();
        }
        return inflated;
    }
}
