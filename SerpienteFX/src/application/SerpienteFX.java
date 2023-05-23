package application;

import java.util.Optional;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import modelo.Comida;
import modelo.Juego;
import modelo.Segmento;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class SerpienteFX extends Application {
    private static final int ANCHURA_JUEGO = 20;
    private static final int ALTURA_JUEGO = 20;
    private static final int TAMANIO_CELDA = 20;
    
    private int puntuacion = 0;
    private Label puntuacionTotal;
    private boolean mensajeGameOverMostrado = false;
    private Juego game;
    private Canvas canvas;
    private GraphicsContext gc; // Dibujo de formas
    int direccionX = 1; 
    int direccionY = 0;
    
    
    @Override
    public void start(Stage primaryStage) {
        game = new Juego(ANCHURA_JUEGO, ALTURA_JUEGO);
        canvas = new Canvas(ANCHURA_JUEGO * TAMANIO_CELDA, ALTURA_JUEGO * TAMANIO_CELDA);
        gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, ANCHURA_JUEGO * TAMANIO_CELDA, ALTURA_JUEGO * TAMANIO_CELDA);
        puntuacionTotal = new Label();
        puntuacionTotal.setTranslateX((ANCHURA_JUEGO * TAMANIO_CELDA - puntuacionTotal.getWidth()) / 2); // Establece la posición X
        puntuacionTotal.setTranslateY((ANCHURA_JUEGO * TAMANIO_CELDA - puntuacionTotal.getWidth()) / 2); // Establece la posición Y
        puntuacionTotal.setTextFill(Color.BLACK);
        puntuacionTotal.toFront();
        root.getChildren().add(puntuacionTotal);
        scene.setOnKeyPressed(e -> {
            KeyCode keyCode = e.getCode();
            // Mover la serpiente según la tecla presionada
            if (keyCode == KeyCode.UP) {
            	direccionX = 0;
            	direccionY = -1;
                game.moverSnake(direccionX, direccionY);
            } else if (keyCode == KeyCode.DOWN) {
            	direccionX = 0;
            	direccionY = 1;
                game.moverSnake(direccionX, direccionY);
            } else if (keyCode == KeyCode.LEFT) {
            	direccionX = -1;
            	direccionY = 0;
                game.moverSnake(direccionX, direccionY);
            } else if (keyCode == KeyCode.RIGHT) {
            	direccionX = 1;
            	direccionY = 0;
                game.moverSnake(direccionX, direccionY);
            }
            
            // Actualizar el estado del juego y redibujar el tablero
            actualizarEstadoJuego();
            dibujarZonaJuego();
        });

        primaryStage.setTitle("SerpienteFX");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Redibujar el tablero inicialmente
        dibujarZonaJuego();
        
        // Iniciar el bucle de animación del juego
        AnimationTimer animationTimer = new AnimationTimer() {
        	private long lastUpdate = 0;
            @Override
            public void handle(long now) {
            	long elapsedNanoSeconds = now - lastUpdate;
                double elapsedSeconds = elapsedNanoSeconds / 1_000_000_000.0;
                
                if (elapsedSeconds >= 1) { // Cambiar el valor según la velocidad deseada
                    game.moverSnake(direccionX, direccionY);
                    lastUpdate = now;
                    
                    
                }

                // Actualizar el estado del juego y redibujar el tablero
                actualizarEstadoJuego();
                dibujarZonaJuego();
                
            }
        };
        animationTimer.start();
        
    }
    
    private void dibujaComida() {
        Comida comida = game.getComida();
        
        // Dibujar el círculo de la comida
        int x = comida.getPosX() * TAMANIO_CELDA + TAMANIO_CELDA / 2;
        int y = comida.getPosY() * TAMANIO_CELDA + TAMANIO_CELDA / 2;
        int radius = TAMANIO_CELDA / 2;
        gc.setFill(Color.RED);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
    
    private void dibujarZonaJuego() {
        gc.clearRect(0, 0, ANCHURA_JUEGO * TAMANIO_CELDA, ALTURA_JUEGO * TAMANIO_CELDA);
        
        // Dibujar la serpiente
        for (Segmento segment : game.getSnake().getCuerpo()) {
            int x = segment.getPosX() * TAMANIO_CELDA;
            int y = segment.getPosY() * TAMANIO_CELDA;
            gc.setFill(Color.GREEN);
            gc.fillRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);
        }
        dibujaComida();
        
   }

    private void actualizarEstadoJuego() {
    	if (game.getSnake().getHead().getPosX() == game.getComida().getPosX()
                && game.getSnake().getHead().getPosY() == game.getComida().getPosY()) {    		
    		incrementarPuntuacion();
            game.comerComida();
        }
    	if (game.isGameOver() && !mensajeGameOverMostrado) {
    		mostrarGameOver();	
    		mensajeGameOverMostrado = true;
    	}
    }
    
    private void incrementarPuntuacion() {
        puntuacion++;
        puntuacionTotal.setText(Integer.toString(puntuacion));
    }

    private void mostrarGameOver() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("¡Game Over! Has perdido.");

            ButtonType restartButton = new ButtonType("Reiniciar");
            alert.getButtonTypes().setAll(restartButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == restartButton) {
                reiniciarJuego();
            }
        });
    }
    
    private void reiniciarJuego() {
        // Restablecer el estado del juego a su configuración inicial
        game = new Juego(ANCHURA_JUEGO, ALTURA_JUEGO);
        mensajeGameOverMostrado = false;
        // Limpiar el tablero
        gc.clearRect(0, 0, ANCHURA_JUEGO * TAMANIO_CELDA, ALTURA_JUEGO * TAMANIO_CELDA);
        // Redibujar el tablero inicialmente
        dibujarZonaJuego();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}