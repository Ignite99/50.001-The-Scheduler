package com.example.infosys_50001;

import java.util.Collections;

//Sze Han's side of the code have not implemented, trying to find out how to push to firebase

public class Schedule{
    java.util.ArrayList<TimeBlock> data = new java.util.ArrayList<TimeBlock>();
    public Schedule(){;}
    // public Schedule(String filename){
    // 	//ics file
    // 	java.io.FileInputStream instream = file_to_fileinputstream(filename);
    // 	this.data = inputstream_to_timeblockarraylist(instream);
    // }
    public Schedule(String ics_string){
        java.io.InputStream instream = new java.io.ByteArrayInputStream(ics_string.getBytes());
        this.data = inputstream_to_timeblockarraylist(instream);
    }
    public Schedule(java.io.InputStream instream){
        this.data = inputstream_to_timeblockarraylist(instream);
    }
    public Schedule(String from,String to){
        TimeBlock tb = new TimeBlock(from,to);
        this.data.add(tb);
    }
    public Schedule(java.time.LocalDateTime from,java.time.LocalDateTime to){
        TimeBlock tb = new TimeBlock(from,to);
        this.data.add(tb);
    }
    public Schedule(TimeBlock tb){
        this.data.add(tb);
    }
    public Schedule(java.util.ArrayList<TimeBlock> data){
        this.data=data;
    }
    public Schedule exclude(TimeBlock event){
        java.util.ArrayList<TimeBlock> returns = new java.util.ArrayList<TimeBlock>();
        for (TimeBlock tb:this.getData()){
            // System.out.println("tb: "+tb);
            // System.out.println("event: "+event);
            if (tb.overlaps(event)){
                TimeBlock[] exclusion = tb.exclude(event);
                Collections.addAll(returns, exclusion);
            } else{
                returns.add(tb);
            }
        }
        return new Schedule(returns);
    }
    public Schedule exclude(Schedule busy_schedule){
        Schedule returns = this.clone();
        for (TimeBlock busyEvent:busy_schedule.getData()){
            returns = returns.exclude(busyEvent);
        }
        return returns;
    }
    //     java.io.FileInputStream file_to_fileinputstream(String filename){
//         java.io.FileInputStream fis;
//         try{
//             fis = new java.io.FileInputStream(filename);
//             return fis;
//         }catch(Exception e) {
//             e.printStackTrace();
//             return null;
//         }
//     }
    java.util.ArrayList<TimeBlock> eventstringarrraylist_to_timeblockarraylist(java.util.ArrayList<String> in_arraylist){
        //Converts an ArrayList of String lines {BEGIN:VEVENT,DESCRIP...,ENDLVEVENT} into an ArrayList of TimeBlocks
        //If one-off event, returns ArrayList with single TimeBlock
        //If repeat event, returns ArrayList with multiple TimeBlocks
        String[] key_lines = new String[3];
        //key_lines[0] is DTSTART
        //key_lines[1] is DTEND
        //key_lines[2] is RRULE
        for (String line:in_arraylist){
            switch(line.substring(0,6)){
                case "DTSTAR":
                    key_lines[0] = line;
                    break;
                case "DTEND:":
                    key_lines[1] = line;
                    break;
                case "RRULE:":
                    key_lines[2] = line;
                    break;
            }
        }
        java.util.ArrayList<TimeBlock> result = new java.util.ArrayList<TimeBlock>();
        String from = key_lines[0].split(":",2)[1];
        String to = key_lines[1].split(":",2)[1];
        TimeBlock event_timeblock = new TimeBlock(from,to);
        result.add(event_timeblock);
        if (key_lines[2] != null){
            String remain = key_lines[2].split(":",2)[1];
            String[] vals = remain.split(";",-1);
            com.example.infosys_50001.FREQ freq = null;
            java.time.LocalDateTime until = null;
            int interval=1;
            int count=1;
            for (String clause:vals){
                String[] c = clause.split("=",2);
                switch(c[0]){
                    case "FREQ":
                        freq = com.example.infosys_50001.FREQ.valueOf(c[1]);
                        break;
                    case "UNTIL":
                        until = TimeBlock.string_to_obj(c[1]);
                        break;
                    case "INTERVAL":
                        interval = Integer.parseInt(c[1]);
                        break;
                    case "COUNT":
                        count = Integer.parseInt(c[1]);
                        break;

                }
            }
            if (freq != null){
                TimeBlock newblock = event_timeblock.clone(freq,interval);
                if (until != null){
                    while (newblock.fromObj().isBefore(until)){
                        result.add(newblock);
                        newblock = newblock.clone(freq,interval);
                    }
                }else if (count > 1){
                    count--;
                    while (count>0){
                        result.add(newblock);
                        newblock = newblock.clone(freq,interval);
                        count--;
                    }
                }
            }

        }
        return result;
    }
    java.util.ArrayList<TimeBlock> inputstream_to_timeblockarraylist(java.io.InputStream in_stream){
        try{
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(in_stream));
            String line;
            boolean estart = true;
            java.util.ArrayList<TimeBlock> all_event_arraylist = new java.util.ArrayList<TimeBlock>();
            java.util.ArrayList<String> single_event_arraylist = new java.util.ArrayList<String>();
            while ((line=br.readLine()) != null){
                if (line.equals("END:VEVENT")){
                    estart=false;
                }
                if (estart){
                    single_event_arraylist.add(line);
                }else {
                    all_event_arraylist.addAll(eventstringarrraylist_to_timeblockarraylist(single_event_arraylist));
                    single_event_arraylist.clear();
                    estart=true;
                }
            }
            return all_event_arraylist;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public java.util.ArrayList<TimeBlock> getData(){
        return this.data;
    }
    @Override
    public String toString(){
        String out="";
        for (TimeBlock tb:data){
            out=out+tb.toString()+"\n";
        }
        return out;
    }
    public String toIcs(){
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (TimeBlock tb:data){
            sb.append(tb.toIcs());
        }
        return sb.toString();
    }
    public Schedule clone(){
        return new Schedule(this.data);
    }
    public static void main(String[] args){
        ;
    }
}