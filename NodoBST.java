public class NodoBST<K, V> {
    protected K clave;
    protected V valor;
    protected NodoBST<K, V> izquierdo;
    protected NodoBST<K, V> derecho;

    public NodoBST(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
        this.izquierdo = this.derecho = null;
    }

    public K getClave() { return clave; }
    public V getValor() { return valor; }
    public NodoBST<K, V> getIzquierdo() { return izquierdo; }
    public NodoBST<K, V> getDerecho() { return derecho; }
    public void setValor(V valor) { this.valor = valor; }
    public void setIzquierdo(NodoBST<K, V> izquierdo) { this.izquierdo = izquierdo; }
    public void setDerecho(NodoBST<K, V> derecho) { this.derecho = derecho; }
}