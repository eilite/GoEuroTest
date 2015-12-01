package com.goeuro;

public class Main {

    public static void main(String[] args){
        if(args.length<1){
            System.out.println("A city is required in parameter\n E.g : java -jar GoEuroTest.jar Berlin");
        }else{
            GoEuroTest goeurotest = new GoEuroTest(args[0]);
            goeurotest.run();
        }
    }
}