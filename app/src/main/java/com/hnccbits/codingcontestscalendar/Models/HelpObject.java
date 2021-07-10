package com.hnccbits.codingcontestscalendar.Models;

public class HelpObject {
    private String question;
    private String answer;
    private boolean expansion=false;

    public HelpObject(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public boolean isExpanded(){return expansion;}
    public void setExpansion(boolean expansion){
        this.expansion=expansion;
    }
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
