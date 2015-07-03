package ru.gubkin.lk.arduinoworksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.terminal.Terminal;

/**
 * Created by root on 29.06.15.
 */
public class TerminalAdapter extends BaseAdapter {
    private final Terminal terminal;
    private LayoutInflater inflater;

    public TerminalAdapter(Context context, Terminal terminal) {
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
        convertView = inflater.inflate(R.layout.terminal_item, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.text_terminal);
        terminal.setView(textView);
        return convertView;
    }
}
