package modelo;

import java.util.Random;

public class Juego {
	private int anchoJuego;
	private int altoJuego;
	private Snake snake;
	private Comida comida;
	private boolean gameOver;
	
	public Juego(int anchoJuego, int altoJuego) {
		this.anchoJuego = anchoJuego;
		this.altoJuego = altoJuego;
		this.snake = new Snake(1, anchoJuego / 2, altoJuego / 2);
		this.comida = crearComida();
		this.gameOver = false;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void generarComida() {
	    Random random = new Random();

	    int x = random.nextInt(anchoJuego - 1);
	    int y = random.nextInt(altoJuego - 1);
	    Comida nuevaComida = new Comida(x, y);
	    
	    setComida(nuevaComida);
	}
	
	public Comida crearComida() {
		Random random = new Random();
		
		int x = random.nextInt(anchoJuego - 1);
		int y = random.nextInt(altoJuego - 1);
		return new Comida(x,y);
	}

	public Snake getSnake() {
		return snake;
	}
	
	public Comida getComida() {
		return comida;
	}

	public void setComida(Comida comida) {
		this.comida = comida;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	public void moverSnake(int direccionX, int direccionY) {
		if(!gameOver) { //Si el juego no esta acabado se permite mover la serpiente
			snake.mover(direccionX, direccionY);
		}
		if(snake.getCuerpo().getFirst().getPosX() == comida.getPosX() && 
				snake.getCuerpo().getFirst().getPosY() == comida.getPosY()) { // Si la serpiente coincide con la comida la serpiente crece
		snake.crecer(direccionX, direccionY);
		generarComida(); // Y se genera mas comida
		}
		Segmento cabeza = snake.getCuerpo().getFirst(); // Se busca la cabeza de las serpiente
		if(cabeza.getPosX() < 0 || cabeza.getPosX() >= anchoJuego || cabeza.getPosY() >= altoJuego) { // Verifica si la posicion de la cabez en el eje X es menor a 0
			setGameOver(true);																		// que significa que esta a la izquierda del limite y si la cabeza esta
																									// mas arriba que al ancho y el alto del juego
		}
		for (int i = 1; i < snake.getCuerpo().size(); i++) { // Bucle para comprobar si la cabeza a 
			Segmento segmento = snake.getCuerpo().get(i); // chocado con el cuerpo
			if(segmento.getPosX() == cabeza.getPosX() && segmento.getPosY() == cabeza.getPosY()) {
				setGameOver(true);
				
			}
		}
	}
	public void comerComida() {
        Comida comida = getComida();
        Snake snake = getSnake();
        Segmento head = snake.getHead();

        if (head.getPosX() == comida.getPosX() && head.getPosY() == comida.getPosY()) { //Si la cabeza de la serpiente está en la misma posición que la comida
            Segmento newSegment = new Segmento(comida.getPosX(), comida.getPosY()); 
            snake.getCuerpo().add(newSegment); // Aumentar el tamaño de la serpiente agregando un nuevo segmento en la posición de la comida
            generarComida(); // Se genera otro punto de comida

        }
    }

}
	
