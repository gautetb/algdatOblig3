import javax.print.DocFlavor;
import java.util.*;

public class ObligSBinTre<T> implements Beholder<T> {

    private static final class Node<T> {
        private T verdi;
        private Node<T> venstre, høyre;
        private Node<T> forelder;

        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder) {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }
    }

    private Node<T> rot;
    private int antall;
    private int endringer;
    private final Comparator<? super T> comp;

    public ObligSBinTre(Comparator<? super T> c) {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;
        int cmp = 0;

        while(p != null) {
            q = p;
            cmp = comp.compare(verdi,p.verdi);
            p = cmp < 0 ? p.venstre : p.høyre;
        }

        p = new Node<>(verdi, q);

        if (q == null) {
            rot = p;
        } else if (cmp < 0){
            q.venstre = p;
        } else {
            q.høyre = p;
        }

        antall++;

        return true;


    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null)
            return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

//oppgave 5.
    //Der kan du kopiereProgramkode5.2 8 d), men i tillegg må du gjøre de endringene som trengs for at pekerenforelderfår korrekt verdi i  alle  noder  etter  en  fjerning.
    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot, q = null;

        while (p != null)
        {
            int cmp = comp.compare(verdi,p.verdi);
            if (cmp < 0) { q = p; p = p.venstre; }
            else if (cmp > 0) { q = p; p = p.høyre; }            else break;
        }
        if (p == null) return false;

        if (p.venstre == null || p.høyre == null)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;

            if (b != null) b.forelder = q;

            if (p == rot) rot = b;
            else if (p == q.venstre) q.venstre = b;
            else q.høyre = b;
        }
        else
        {
            Node<T> s = p, r = p.høyre;
            while (r.venstre != null)
            {
                s = r;
                r = r.venstre;
            }

            p.verdi = r.verdi;


            if (r.høyre != null) r.høyre.forelder = s;

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;
        endringer++;
        return true;
    }



   //Den  skal fjerne  alle  forekomstene  avverdii  treet.
    //Husk  at  duplikater  er  tillatt.
    //ermed  kan  en  og samme verdi ligge flere steder i treet.
    //Metoden skal returnere antallet som ble fjernet. Hvis treet  er  tomt,  skal  0  returneres.
    public int fjernAlle(T verdi){
        int antallFjernet = 0;
        while (fjern(verdi)) antallFjernet++;
        return antallFjernet;


    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        Node<T> p = rot;

        int antallVerdi = 0;

        while (p != null) {
            int cmp = comp.compare(verdi,p.verdi);

            if (cmp < 0) {
                p = p.venstre;
            } else {
                if (cmp == 0) {
                    antallVerdi++;
                }
                p = p.høyre;
            }
        }

        return antallVerdi;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

  //Den  skal traversere (rekursivt eller iterativt) treet i eneller annen rekkefølge og sørge for at samtlige pekere
    // og  nodeverdier  i  treet  blir  nullet.
    //Det  er  med  andre  ord  ikke  tilstrekkelig  å setterottilnullogantalltil 0.
    @Override
    public void nullstill() {
      //  if (!tom()) nullstill(rot);
        rot = null;
        antall = 0;
        endringer++;

    }

    private static <T> Node<T> nesteInorden(Node<T> p) {

        if (p.høyre != null) {

            p = p.høyre;
            // finn lengst ned til venstre i subtre
            while (p.venstre!= null) {
                p = p.venstre;
            }

            return p;
        }  else {

            while (p.forelder != null && p.forelder.høyre == p) {
                p = p.forelder;
            }
            return p.forelder;
        }

    }

    private static <T> Node<T> førsteInorden(Node<T> p) {

        while (p.venstre != null) {
            p = p.venstre;
        }

        return p;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");

        if (!tom()) {
            Node<T> p = førsteInorden(rot);
            s.append(p.verdi);

            p = nesteInorden(p);

            while (p != null) {
                s.append(", ").append(p.verdi);
                p = nesteInorden(p);
            }
        }

        s.append("]");

        return s.toString();
    }

   //Oppgave4
    //Den skal gjøre som metodentoString(), men med verdiene i motsatt rekkefølge.
    //Du skal løse dette ved å traversere treet i omvendt inorden  (dvs.  motsatt  vei  av  inorden) iterativt.
    //erskaldu  bruke  en  hjelpestakk  (og  ikke rekursjon).
    // F.eks. enTabellStakkeller en stakk fra java.util (f.eks. enArrayDeque).
    //oden din skal ikke noe  sted  benytte  forelderpekerne.
    //koden din også kunne virke  i  et  binærtreuten  forelderpekere.
    public String omvendtString(){

        //throw new UnsupportedOperationException("Ikke kodet ennå!");
        if (tom()) return "[]";
        Stakk<Node<T>> stakk = new TabellStakk<>();
        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = rot;
        while (p.høyre != null)
        {
            stakk.leggInn(p);
            p = p.høyre;
        }

        s.add(p.verdi.toString());

        while (true)
        {
            if (p.venstre != null)
            {
                p = p.venstre;
                while (p.høyre != null)
                {
                    stakk.leggInn(p);
                    p = p.høyre;
                }
            }
            else if (!stakk.tom()) p = stakk.taUt();
            else break;

            s.add(p.verdi.toString());
        }

        return s.toString();
    }


    public String høyreGren() {
        StringJoiner s = new StringJoiner(", ","[","]");

        if (!tom()) {
            Node<T> p = rot;
            s.add(p.verdi.toString());

            while(p.høyre != null || p.venstre != null) {

                if (p.høyre != null) {
                    p = p.høyre;
                    s.add(p.verdi.toString());

                } else if (p.venstre != null) {
                    p = p.venstre;
                    s.add(p.verdi.toString());
                }
            }

        }

        return s.toString();

    }

    public String lengstGren() {
        StringJoiner s = new StringJoiner(", ", "[", "]");

        if (tom()) {
            return "[]";
        }

        ArrayDeque<Node<T>> tempStack = new ArrayDeque<>();
        ArrayDeque<Node<T>> finalStack = new ArrayDeque<>();
        Node<T> p = førsteInorden(rot);

        int i = 0;

        int lengstvei = 0;

        while (i < antall) {


            if (p.venstre == null && p.høyre == null) {

                Node<T> e = p;
                tempStack.add(e);


                int veilengde = 1;


                while (e.forelder != null) {
                    e = e.forelder;

                    tempStack.push(e);

                    veilengde++;
                }

                if (veilengde > lengstvei ) {
                    lengstvei = veilengde;
                    finalStack = tempStack.clone();
                }

                tempStack.clear();
            }

            p = nesteInorden(p);
            i++;
        }

        while (!finalStack.isEmpty()) {
            s.add(finalStack.pop().toString());
        }

        return s.toString();
    }


    public String[] grener() {
        int bladnoder = 0;

        if (tom()) {
            return new String[]{};
        }

        ArrayDeque<Node<T>> stack = new ArrayDeque<>();
        ArrayDeque<String> stringStack = new ArrayDeque<>();
        Node<T> p = førsteInorden(rot);

        int i = 0;

        while (i < antall) {


            if (p.venstre == null && p.høyre == null) {
                StringJoiner s = new StringJoiner(", ", "[", "]");
                Node<T> e = p;
                stack.add(e);

                bladnoder++;


                while (e.forelder != null) {
                    e = e.forelder;
                    stack.push(e);
                }

                while (!stack.isEmpty()) {
                    s.add(stack.pop().toString());
                }


                stringStack.push(s.toString());

                stack.clear();
            }

            p = nesteInorden(p);
            i++;
        }

        String[] liste = new String[bladnoder];
        int e = bladnoder-1;

        while (!stringStack.isEmpty()) {

            liste[e] = stringStack.pop();

            e--;
        }

        return liste;
    }

    public String blad(Node<T> p) {
        if (p.venstre == null && p.høyre == null) {
            return p.verdi.toString();
        } else if (p.venstre != null && p.høyre != null) {
            return blad(p.venstre) + ", "+blad(p.høyre);
        } else if (p.venstre != null) {
            return blad(p.venstre);
        } else {
            return blad(p.høyre);
        }
    }

    public String bladnodeverdier() {
        StringJoiner s = new StringJoiner(",","[","]");

        if (tom()) {
            return "[]";
        }

        s.add(blad(rot));

        return s.toString();
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {

        while (true) {
            if (p.venstre != null) p = p.venstre;
            else if (p.høyre != null) p = p.høyre;
            else return p;
        }

    }

    public String postString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext() {
            return p != null;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    }
}



