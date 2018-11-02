import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        int [] a = {4,7,2,9,4,10,8,7,4,6,1};

        int[] b = {1,8,2,4,7,5,6,6};

        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        ObligSBinTre<Character> tre2 = new ObligSBinTre<>(Comparator.naturalOrder());

        for (int verdi : a) tre.leggInn(verdi);

        char[] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();
        for(char c : verdier) tre2.leggInn(c);

        String[] s = tre2.grener();

        System.out.println(tre2.bladnodeverdier());



    }
}
