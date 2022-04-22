package com.example.infosys_50001;

import androidx.annotation.NonNull;

//Sze Han's side of the code have not implemented, trying to find out how to push to firebase

public class TimeBlock{
    //TimeBlock is immutable
    //20220125T100000
    java.time.LocalDateTime from_obj;
    java.time.LocalDateTime to_obj;
    static java.time.format.DateTimeFormatter ics_format = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    public static java.time.LocalDateTime string_to_obj(String in_string){
        //e.g of in_string
        //20220125T100000
        java.time.LocalDateTime obj = java.time.LocalDateTime.parse(in_string,ics_format);
        return obj;
    }
    public static String obj_to_string(java.time.LocalDateTime in_datetime){
        String out_str = in_datetime.format(ics_format);
        return out_str;
    }
    public TimeBlock(String from,String to){
        this.from_obj = string_to_obj(from);
        this.to_obj = string_to_obj(to);
    }
    public TimeBlock(java.time.LocalDateTime from,java.time.LocalDateTime to){
        this.from_obj = from;
        this.to_obj = to;
    }
    @NonNull
    public String toString(){
        return "######################\nfrom: "+from_obj.toString()+"\n  to: "+to_obj.toString()+"\n######################";
    }
    public String toIcs(){
        return
                "BEGIN:VEVENT\n" +
                        "DTSTART:" +
                        obj_to_string(this.from_obj) +
                        "\n" +
                        "DTEND:" +
                        obj_to_string(this.to_obj) +
                        "\n" +
                        "END:VEVENT\n";
    }
    public int duration(){
        long start = from_obj.toEpochSecond(java.time.ZoneOffset.UTC);
        long end = to_obj.toEpochSecond(java.time.ZoneOffset.UTC);
        return (int) ((end-start)/60);
    }
    public java.time.LocalDateTime fromObj(){
        return this.from_obj;
    }
    public java.time.LocalDateTime toObj(){
        return this.to_obj;
    }
    public Boolean overlaps(TimeBlock target){
        //returns true if TimeBlockA.in(TimeBlockB)
        return !(this.from_obj.isAfter(target.to_obj) |
                this.to_obj.isBefore(target.from_obj) |
                this.from_obj.isEqual(target.to_obj) |
                this.to_obj.isEqual(target.from_obj)
        );
    }
    public TimeBlock[] exclude(TimeBlock B){
        //returns self exclude B as TimeBlock array. Could contain no blocks, one block or two blocks
        //if self and B don't overlap, returns self
        //if self is "larger" than B, use B to "shave" time off of A and returns "remainder"
        //if B is "larger" than self, returns empty array
        if (! B.overlaps(this)){
            return new TimeBlock[]{this};
        } else if (B.from_obj.isBefore(this.from_obj) && B.to_obj.isAfter(this.to_obj)){
            return new TimeBlock[0];
        } else {
            TimeBlock[] temp = new TimeBlock[2];
            if (B.from_obj.isAfter(this.from_obj)){
                TimeBlock block1 = new TimeBlock(this.from_obj,B.from_obj);
                temp[0] = block1;
            }
            if (B.to_obj.isBefore(this.to_obj)){
                TimeBlock block2 = new TimeBlock(B.to_obj,this.to_obj);
                temp[1] = block2;
            }
            if (temp[0] == null){
                return new TimeBlock[] {temp[1]};
            } else if (temp[1] == null){
                return new TimeBlock[] {temp[0]};
            }else{
                return temp;
            }
        }

    }
    public TimeBlock clone(com.example.infosys_50001.FREQ freq, int value){
        java.time.LocalDateTime new_from_obj = this.from_obj;
        java.time.LocalDateTime new_to_obj = this.to_obj;
        switch(freq){
            case SECONDLY:
                new_from_obj=new_from_obj.plusSeconds(value);
                new_to_obj=new_to_obj.plusSeconds(value);
                break;
            case MINUTELY:
                new_from_obj=new_from_obj.plusMinutes(value);
                new_to_obj=new_to_obj.plusMinutes(value);
                break;
            case HOURLY:
                new_from_obj=new_from_obj.plusHours(value);
                new_to_obj=new_to_obj.plusHours(value);
                break;
            case DAILY:
                new_from_obj=new_from_obj.plusDays(value);
                new_to_obj=new_to_obj.plusDays(value);
                break;
            case WEEKLY:
                new_from_obj=new_from_obj.plusWeeks(value);
                new_to_obj=new_to_obj.plusWeeks(value);
                break;
            case MONTHLY:
                new_from_obj=new_from_obj.plusMonths(value);
                new_to_obj=new_to_obj.plusMonths(value);
                break;
            case YEARLY:
                new_from_obj=new_from_obj.plusYears(value);
                new_to_obj=new_to_obj.plusYears(value);
                break;
        }
        return new TimeBlock(new_from_obj,new_to_obj);
    }
}