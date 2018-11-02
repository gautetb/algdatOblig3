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

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodetennå!");
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

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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

    public String omvendtString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
        StringJoiner s = new StringJoiner(", ","[","]");

        if (!tom()) {
            Node<T> p = rot;
            s.add(p.verdi.toString());

            while(p.høyre != null || p.venstre != null) {

            }

        }

        return s.toString();
    }

    public String[] grener() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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



