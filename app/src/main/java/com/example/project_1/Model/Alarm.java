package com.example.project_1.Model;

public class Alarm {
    private int stt;
    private String hour;
    private String min;
    private String repeat;
    private String status;

    public Alarm(int stt, String hour, String min, String repeat, String status) {
        this.stt = stt;
        this.hour = hour;
        this.min = min;
        this.repeat = repeat;
        this.status = status;
    }

    public Alarm() {

    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
