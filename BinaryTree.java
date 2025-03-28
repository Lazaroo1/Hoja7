import java.util.*;

public class BinaryTree<K extends Comparable<K>, V> {
    private NodoBST<K, V> raiz;

    public BinaryTree() {
        raiz = null;
    }

    public void insertar(K clave, V valor) {
        raiz = insertarRec(raiz, clave, valor);
    }

    private NodoBST<K, V> insertarRec(NodoBST<K, V> nodo, K clave, V valor) {
        if (nodo == null) {
            return new NodoBST<>(clave, valor);
        }

        int cmp = clave.compareTo(nodo.getClave());
        if (cmp < 0) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), clave, valor));
        } else if (cmp > 0) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), clave, valor));
        } else {
            nodo.setValor(valor);
        }
        return nodo;
    }

    public V buscar(K clave) {
        return buscarRec(raiz, clave);
    }

    private V buscarRec(NodoBST<K, V> nodo, K clave) {
        if (nodo == null) return null;
        
        int cmp = clave.compareTo(nodo.getClave());
        if (cmp == 0) return nodo.getValor();
        return cmp < 0 ? buscarRec(nodo.getIzquierdo(), clave) : buscarRec(nodo.getDerecho(), clave);
    }

    public List<V> inOrder() {
        List<V> resultado = new ArrayList<>();
        inOrderRec(raiz, resultado);
        return resultado;
    }

    private void inOrderRec(NodoBST<K, V> nodo, List<V> resultado) {
        if (nodo != null) {
            inOrderRec(nodo.getIzquierdo(), resultado);
            resultado.add(nodo.getValor());
            inOrderRec(nodo.getDerecho(), resultado);
        }
    }

    public boolean estaVacio() {
        return raiz == null;
    }
}