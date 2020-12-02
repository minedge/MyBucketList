package com.example.mybucketlist;

public class PersonalItem {
    public String ident, title, locate, complete, date;

    public PersonalItem(String _ident, String _title, String _locate, String _complete, String _date){
        this.ident = _ident;
        this.title = _title;
        this.locate = _locate;
        this.complete = _complete;
        this.date = _date;
    }
}
