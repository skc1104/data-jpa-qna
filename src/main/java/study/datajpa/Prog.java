package study.datajpa;

import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class Prog implements CommandLineRunner {


    public static void main(String[] args) throws Exception {
        new Prog().run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        //[Optional 생서하기]
        Optional<String> optional = Optional.empty();
        System.out.println(optional);
        System.out.println(optional.isPresent());

        Optional<String> op2 = Optional.ofNullable(getName());
        String name = op2.orElse("It was null");
        System.out.println("name = " + name);

        //[Optional 사용하기]
        //Old
        List<String> names = getNames();
        List<String> tempNames = names != null ? names : new ArrayList<>();
        System.out.println("tempNames = " + tempNames);

        //List<String> nameList = Optional.ofNullable(getNames()).get();
        List<String> nameList = Optional.ofNullable(getNames()).orElseGet(() -> new ArrayList<>());
        System.out.println("nameList = " + nameList);

        //[Optional 활용 예시 (2)]
        name = getName();
        String result = "";
        try {
            result = name.toUpperCase();
        } catch(NullPointerException e) {
            //Ex) throw new CustomUpperCaseException();
        }

        //[OrElse와 orElseGet 차이 예시]
        String userEmail = "ASGIVEN@gold.com";
        String reuslt1 = Optional.ofNullable(userEmail).orElse(getUserEmail());
        System.out.println("reuslt1 = " + reuslt1);

        String result2 = Optional.ofNullable(userEmail).orElseGet(this::getUserEmail);
        System.out.println("result2 = " + result2);
    }

    private String getUserEmail() {
        System.out.println("getUserEmail() called");
        return "myaccount@mail.com";
    }

    private List<String> getNames() {
        List<String> result = new ArrayList<>();
        result.add("Hello");
        result.add("Lea");
        //return result;

        return null;
    }

    private String getName() {
        return "Your name";
    }
}
