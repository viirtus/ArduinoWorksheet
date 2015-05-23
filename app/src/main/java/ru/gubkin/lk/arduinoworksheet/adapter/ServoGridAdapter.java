package ru.gubkin.lk.arduinoworksheet.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;

import ru.gubkin.lk.arduinoworksheet.R;
import ru.gubkin.lk.arduinoworksheet.component.servo.OnControlClickListener;
import ru.gubkin.lk.arduinoworksheet.component.servo.RadialScaleView;
import ru.gubkin.lk.arduinoworksheet.component.servo.Servo;
import ru.gubkin.lk.arduinoworksheet.component.servo.ServoDialog;

/**
 * Created by root on 20.05.15.
 */
public class ServoGridAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<Servo> items;
    private final LayoutInflater inflater;

    public ServoGridAdapter(Context context, ArrayList<Servo> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageButton minus;
        ImageButton plus;
        RadialScaleView view;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.servo_item, parent, false);
            holder.plus = (ImageButton) convertView.findViewById(R.id.servo_up_btn);
            holder.minus = (ImageButton) convertView.findViewById(R.id.servo_dwn_btn);
            holder.view = (RadialScaleView) convertView.findViewById(R.id.servo_one);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Servo servo = items.get(position);
        OnControlClickListener listener = new OnControlClickListener(holder.view, servo);
        holder.minus.setOnTouchListener(listener);
        holder.minus.setOnClickListener(listener);
        holder.plus.setOnTouchListener(listener);
        holder.plus.setOnClickListener(listener);
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ServoDialog dialog = new ServoDialog(context, servo);
                dialog.show();
                return true;
            }
        });
//        servo.setServoView(holder.view);

        return convertView;
    }
}
