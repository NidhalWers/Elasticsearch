package org.snp;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

/**
 *
 *  In order to work you need isMaster system property set to either true or false, and set the good ports for master and slave
 *
 */


@QuarkusMain
public class Main {


    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static boolean isMasterTest(){
        return MyApp.isMasterTest;
    }
    public static class MyApp implements QuarkusApplication {


        public static final boolean isMasterTest = System.getProperty("isMaster").equals("true") ;

        @Override
        public int run(String... args)  {
            if(isMasterTest){
                System.out.println("I'm the master");
            }else{
                System.out.println("I'm a slave, my name is "+System.getProperty("name"));
            }
            Quarkus.waitForExit();
            return 0;
        }
    }
}