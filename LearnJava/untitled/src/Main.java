import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Phan");
        names.add("Văn");
        names.add("Thi");
        System.out.println(names.stream().map(new Function<String, Object>() {
            @Override
            public String apply(String s) {
                return s.toUpperCase();
            }
        }).toList());

    }
}