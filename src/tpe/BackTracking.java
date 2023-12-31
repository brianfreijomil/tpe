package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BackTracking {
    
    private Grafo grafo;
    private ArrayList<Arco> arcos;
    private ArrayList<Arco> solucionTuneles;
    private ArrayList<Integer> verticesConsiderados;
    private int poda;
    private int distTotal;
    private int metrica;

    public BackTracking(Grafo grafo, int poda) {
        this.grafo = grafo;
        this.poda = poda;
        this.arcos = new ArrayList<Arco>();
        this.solucionTuneles = new ArrayList<Arco>();
        this.verticesConsiderados = new ArrayList();
    }

    //Este metodo 
    public int backTracking() {
        distTotal=0;
        metrica=0;
        Iterator<Arco> it = grafo.obtenerArcos();
        while (it.hasNext()) {
            Arco arco = it.next();
            if(!arcos.contains(arco))
                arcos.add(arco);
        }
        ArrayList<Arco> actual = new ArrayList<Arco>();
        int pos = 0;
        int sumaArcos = 0;
        this.backTracking(actual,pos,sumaArcos);
        return distTotal;
    }

    private void backTracking(ArrayList<Arco> actual,int pos,int sumaArcos) {
        metrica++;
        if(this.metodoKruskal(actual)) { //solucion posible
            if(distTotal == 0) {
                distTotal = sumaArcos;
                solucionTuneles.addAll(actual);
            }
            else {
                if(distTotal > sumaArcos) {
                    distTotal = sumaArcos;
                    solucionTuneles.clear();
                    solucionTuneles.addAll(actual);
                }
            }
        }
        else { //recorro todas las posibilidades
            int i = pos;
            int j;
            while(i < arcos.size()) {
                j=i;
                Arco arco = arcos.get(i);
                if(!actual.contains(arco)) {
                    actual.add(arco); //agrego
                    sumaArcos += (Integer) arco.getEtiqueta(); //agrego
                        if(sumaArcos <= poda && actual.size() <= grafo.cantidadVertices()-1) {
                            j++;
                            this.backTracking(actual,j,sumaArcos); //sigo explorando
                        }
                    }
                    sumaArcos -= (Integer) arco.getEtiqueta(); //remuevo
                    actual.remove(arco); //elimino
                i++;
            }
        }
    }

    private boolean verificarConsiderados(ArrayList<Arco>list) {
        if(!verticesConsiderados.isEmpty()) {
            verticesConsiderados.clear();
        }
        for (Arco a : list) {
            Integer origen = a.getVerticeOrigen();
            Integer destino = a.getVerticeDestino();
            if(!verticesConsiderados.contains(origen)) {
                verticesConsiderados.add(origen);
            }
            if(!verticesConsiderados.contains(destino)) {
                verticesConsiderados.add(destino);
            } 
        }
        return verticesConsiderados.size() == grafo.cantidadVertices();
    }

    public boolean metodoKruskal(ArrayList<Arco> candidato) {
        if(verificarConsiderados(candidato)) {
            int cantVertices = grafo.cantidadVertices();
            HashMap<Integer,Integer> padres = new HashMap<Integer,Integer>();
            for (int i = 1; i <= cantVertices; i++) {
                padres.put(i, i);
            }
            int iArcos = 0;
            int i = 0;
            int cantArcos = candidato.size();
            while((iArcos<cantVertices-1)&&(i<cantArcos)) {
                Integer origen = candidato.get(i).getVerticeOrigen();
                Integer destino = candidato.get(i).getVerticeDestino();
                if(find(origen,padres)!=find(destino,padres)) {
                    unite(origen, destino,padres);
                    iArcos++;
                }
                i++;
            }
            if(iArcos==cantVertices-1) {
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }

    public int find(int x,HashMap<Integer,Integer>padres) {
        if (padres.get(x) == x) {
            return x;
        }
        return find(padres.get(x),padres);
    }

    public void unite(int x, int y, HashMap<Integer,Integer>padres) {
        int fx = find(x,padres);
        int fy = find(y,padres);
        if(fx == x) {
            padres.replace(fx,y);
        }
        else {
            if(fy == y) {
                padres.replace(y,x);
            }
            else {
              padres.replace(fy,y);
              padres.replace(y,x);
            }
        }
    }

    public int getMetrica() {
        return metrica;
    }

    public ArrayList<Arco> getTuneles() {
        ArrayList<Arco> ret = new ArrayList<Arco>();
        ret.addAll(solucionTuneles);
        return ret;
    }

    public int getDistanciaTotal() {
        return distTotal;
    }

}

