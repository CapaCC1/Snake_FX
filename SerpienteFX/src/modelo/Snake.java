package modelo;

import java.util.LinkedList;

public class Snake {
	private LinkedList<Segmento> cuerpo;

	public Snake(int tamanioInicial, int comienzoX, int comienzoY) {
		this.cuerpo = new LinkedList<Segmento>();
		
		for (int i = 0; i < tamanioInicial; i++) {
			cuerpo.add(new Segmento(comienzoX -i, comienzoY));
		}
	}

	public LinkedList<Segmento> getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(LinkedList<Segmento> cuerpo) {
		this.cuerpo = cuerpo;
	}
	
	public Segmento getHead() {
        return cuerpo.get(0);
    }
	
	public void mover(int direccionX, int direccionY) { // Movimiento de la serpiente
		Segmento cabeza = cuerpo.getFirst();// Crea una cabeza
		int nuevaCabezaX = cabeza.getPosX() + direccionX; // Crea una cabeza en la direccion que se seleccione
		int nuevaCabezaY = cabeza.getPosY() + direccionY;
		Segmento nuevaCabeza = new Segmento(nuevaCabezaX, nuevaCabezaY); // Crea una nueva cabeza en la direccion seleccionada
		cuerpo.addFirst(nuevaCabeza); // Se añade al principio esa cabeza y se convierte en la nueva cabeza
		cuerpo.removeLast(); // Se borra el ultimo segmento para simular el movimiento
	}
	
	public void crecer(int direccionX, int direccionY) { // Metodo para hacer crecer la serpiente añadiendo una nueva cabeza al comer
		Segmento cabeza = cuerpo.getFirst();
		int nuevaCabezaX = cabeza.getPosX() + direccionX;
		int nuevaCabezaY = cabeza.getPosY() + direccionY;
		Segmento nuevaCabeza = new Segmento(nuevaCabezaX, nuevaCabezaY);
		cuerpo.addFirst(nuevaCabeza);
	}
	
	public boolean contienePosicion(int posX, int posY) {
        for (Segmento segmento : cuerpo) {
            if (segmento.getPosX() == posX && segmento.getPosY() == posY) {
                return true;
            }
        }
        return false;
    }
	
	
}
