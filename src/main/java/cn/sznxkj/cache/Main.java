package cn.sznxkj.cache;

import cn.sznxkj.cache.datas.DataProcess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by wangll on 2017/1/15.
 */
public class Main {

    public static void main(String ... args) {
        NCacheClient ncc = new NCacheClient();
        ncc.init();
        ncc.addWork("test",
                new DataProcess<Object>() {
                    @Override
                    public HashMap<String, Object> process(ArrayList<Object> rawData) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("age>50", rawData.stream()
                        .filter((p)->
                            ((Person)p).age>=50
                        )
                        .collect(Collectors.toList()));

                        data.put("average_age", rawData.stream()
                                .mapToInt((p) -> ((Person)p).age)
                                .average())
                                ;
                        return data;
                    }

                    @Override
                    public ArrayList<Object> draw() {
                        return getPersons();
                    }

                    @Override
                    public boolean isStrongCache() {
                        return true;
                    }
                });

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }
        for (;;) {
            try {
                Object o1 = ((ArrayList<Object>) ncc.getByKey("age>50")).get(10);
                Object o2 = ncc.getByKey("average_age");
                ((Person)o1).print();
                System.out.println(o2);
            }catch (Exception e) {}
        }

    }

    static ArrayList<Object> persons;
    static ArrayList<Object> getPersons() {
        java.util.Random r = new java.util.Random();
        persons = new ArrayList<>();
        for (int i=0; i<=100; i++) {
            int j = r.nextInt(100);
            persons.add(new Person(j, "F" + j, "L" + j));
        }
        return persons;
    }

    static class Person {
        int age;
        String firstName;
        String lastName;
        public Person(int age, String fn, String ln) {
            this.age = age;
            this.firstName = fn;
            this.lastName = ln;
        }

        public void print() {
            System.out.println(this.firstName + "·" + this.lastName + "(" + this.age + ")");
        }
    }

}
