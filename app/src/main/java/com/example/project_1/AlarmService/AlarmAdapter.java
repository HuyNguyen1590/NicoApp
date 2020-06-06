package com.example.project_1.AlarmService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_1.DAO.AlarmDAO;
import com.example.project_1.MainActivity;
import com.example.project_1.Model.Alarm;
import com.example.project_1.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends ArrayAdapter {
    TextView time_alarm, time_repeat;
    ImageButton time_delete;
    Context context;
    List<Alarm> alarms;
    int resource;

    public AlarmAdapter (Context context, int resource , List<Alarm> alarms){
        super(context,resource,alarms);
        this.context = context;
        this.alarms = alarms;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,null);
        View v = convertView;
        time_alarm = v.findViewById(R.id.time_alarm);
        time_repeat = v.findViewById(R.id.time_repeat);
        time_delete = v.findViewById(R.id.btn_delete);

        time_alarm.setText(alarms.get(position).getHour()+":"+alarms.get(position).getMin());
        time_repeat.setText(alarms.get(position).getRepeat());

        time_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDAO alarmDAO = new AlarmDAO(v.getContext());
                alarmDAO.deleteAlarm(alarms.get(position));
                alarms.remove(position);
                notifyDataSetChanged();
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlarmDAO alarmDAO = new AlarmDAO(v.getContext());
                MainActivity.alertDialog.show();
                Toast.makeText(v.getContext(), "Update", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return v;
    }
}
